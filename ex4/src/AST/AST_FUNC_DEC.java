package AST;
import IR.IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE return_type;
    public String name;
    public AST_LIST<AST_ARGUMENT> argList;
    public AST_LIST<AST_STMT> stmtList;
    public TYPE_FUNCTION currTypeFunc;
    private int local_var_num;

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_STMT> stmtList, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)() {stmtList}\n", type.type, name);
        this.return_type = type;
        this.name = name;
        this.stmtList = stmtList;
        this.line = line;
        this.local_var_num = 0;
    }

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_ARGUMENT> argList, AST_LIST<AST_STMT> stmtList, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)(argumentsList) {stmtList}\n", type.type, name);
        this.return_type = type;
        this.name = name;
        this.argList = argList;
        this.stmtList = stmtList;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("funcDec: %s\n", this.name);

        if (return_type != null)
            return_type.PrintMe();
        if (argList != null)
            argList.PrintMe();
        if (stmtList != null)
            stmtList.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("funcDec: %s\n", this.name));
        if (return_type != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, return_type.SerialNumber);
        if (argList != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argList.SerialNumber);
        if (stmtList != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmtList.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        boolean validName, isMethod, isFunction;
        ScopeTypeEnum scopeType;
        TYPE_LIST params = null;

        /* Search the type of the function- (class) method or (global) function */
        scopeType = SYMBOL_TABLE.getInstance().getCurrentScopeType();
        if (scopeType != ScopeTypeEnum.CLASS && scopeType != ScopeTypeEnum.GLOBAL)
        {
            throw new SemanticException(this);
        }

        /*  CHECK: check if there are classes/ arrays with func name and func name != "string","int","void"
            case (class) method: check if there are previous declarations with this name in current class and
                                 if there is in a parent class- make sure it has the same signature
            case (global) function: check if there are previous declarations with this name in global scope
         */
        /*  validName = true if there is no variable with this name in current scope*/
        validName = SYMBOL_TABLE.getInstance().findInLastScope(this.name) != null;
        isMethod = scopeType == ScopeTypeEnum.CLASS && !validName;
        isFunction = scopeType ==  ScopeTypeEnum.GLOBAL && !validName;

        if (isMethod || isFunction)
        {
            /*  SemantMe will throw an error if the return type is invalid */
            currTypeFunc = new TYPE_FUNCTION(this.return_type.SemantMe(), this.name, null);
            SYMBOL_TABLE.getInstance().enter(this.name, currTypeFunc, false);
            SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.FUNC, currTypeFunc);

            /* SemantMe() checks parameters:
               1. parameter's type can be instanced (only a "string"/ "int"/ previous declared class/ previous declared array/"void")
               2. parameter's name is not string/ int/ previous declared class/ previous declared array/"void"*/
            if (this.argList != null)
            {
                params = (TYPE_LIST) this.argList.SemantMe();

                /* Set offsets of params (arguments) */
                int argument_var_num = isMethod ? 1 : 0; // First argument of a method is its class ptr
                TYPE_LIST type_pointer = params;
                while (type_pointer != null)
                {
                    ((TYPE_VAR) type_pointer.head).set_argument(argument_var_num);
                    argument_var_num++;
                    type_pointer = type_pointer.tail;
                }
            }

            currTypeFunc.params = params;

            /* Make sure there is only an override and no overload or using with function's name */
            if (isMethod)
            {
                isValidMethod(this.name, currTypeFunc);
                currTypeFunc.offset = 4 * (SYMBOL_TABLE.getInstance().getCurrentClass().numMethods++);
                currTypeFunc.isMethod = true;
            }

            /* If the return type isn't match or if there is a semantic error inside the scope- SemantMe() will throw an error*/
            if (this.stmtList != null)
            {
                this.stmtList.SemantMe();
                this.local_var_num = currTypeFunc.num_local_variables;
            }

            SYMBOL_TABLE.getInstance().endScope();
            return currTypeFunc;
        }
        throw new SemanticException(this);
    }

    /* Receives name of the given new function and the function itself
       Throw an error if there is an overloading or a Cfield with this name*/
    public void isValidMethod(String name, TYPE_FUNCTION overrideMethod) throws SemanticException
    {
        TYPE_CLASS fatherClass = (SYMBOL_TABLE.getInstance().getCurrentClass()).father;
        TYPE currType = SYMBOL_TABLE.getInstance().findInInheritance(name, fatherClass);
        if (currType != null)
        {
            if (currType instanceof TYPE_FUNCTION) {
                /* Different signatures- overload*/
                if (!overrideMethod.equals(currType)) {
                    throw new SemanticException(this);
                }
            } else {
                /* There is a variable with the same name in a parent class (and it's not a function) */
                throw new SemanticException(this);
            }
        }
    }

    @Override
    public TEMP IRme()
    {
        IR.getInstance().Add_IRcommand(new IRcommand_Start_Func(this.currTypeFunc.func_label, this.local_var_num));
        stmtList.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_End_Func(this.currTypeFunc.epilogue_func_label));
        return null;
    }
}