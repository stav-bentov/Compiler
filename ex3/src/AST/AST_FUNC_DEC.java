package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.HashSet;
import java.util.Set;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE return_type;
    public String name;
    public AST_LIST<AST_ARGUMENT> argList;
    public AST_LIST<AST_STMT> stmtList;

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_STMT> stmtList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)() {stmtList}\n", type.type, name);
        this.return_type = type;
        this.name = name;
        this.stmtList = stmtList;
    }

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_ARGUMENT> argList, AST_LIST<AST_STMT> stmtList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)(argumentsList) {stmtList}\n", type.type, name);
        this.return_type = type;
        this.name = name;
        this.argList = argList;
        this.stmtList = stmtList;
    }

    public void PrintMe() {
        System.out.print("AST_FUNC_DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        return_type.PrintMe();
        if (argList != null) argList.PrintMe();
        stmtList.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("func_dec: %s\n", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, return_type.SerialNumber);
        if (argList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argList.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmtList.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        boolean validName, isMethod, isFunction;
        TYPE_FUNCTION currTypeFunc;
        ScopeTypeEnum scopeType;
        TYPE_LIST params = null;

        /* Search the type of the function- (class) method or (global) function */
        scopeType = SYMBOL_TABLE.getInstance().getCurrentScopeType();
        if (scopeType != ScopeTypeEnum.CLASS && scopeType != ScopeTypeEnum.GLOBAL)
        {
            throw new SemanticException("Function: %s is not declared in a Global scope or in a Class scope", this);
        }

        /*  CHECK: check if there are classes/ arrays with func name and func name != "string","int","void"
            case (class) method: check if there are previous declarations with this name in current class and
                                 if there is in a parent class- make sure it has the same signature
            case (global) function: check if there are previous declarations with this name in global scope
         */
        /* TODO: because Im not sure - if this.name = string, void, int for method what to do?
        *  validName = true if there is no variable with this name in current scope*/
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
            }

            currTypeFunc.params = params;

            /* Make sure there is only an override and no overload or using with function's name */
            if (isMethod)
                isValidMethod(this.name, currTypeFunc);

            /* If the return type isn't match or if there is a semantic error inside the scope- SemantMe() will throw an error*/
            if (this.stmtList != null)
                this.stmtList.SemantMe();

            SYMBOL_TABLE.getInstance().endScope();
            return currTypeFunc;
        }
        throw new SemanticException("Duplicated definitions named: %s", this);
    }

    /* Recieves name of the given new function and the function itself
       Throw an error if there is an overloading or a Cfield with this name*/
    public void isValidMethod(String name, TYPE_FUNCTION overrideMethod) throws SemanticException
    {
        TYPE_CLASS fatherClass = ((TYPE_CLASS) SYMBOL_TABLE.getInstance().getCurrentClass()).father;
        TYPE currType = SYMBOL_TABLE.getInstance().findInInheritance(name, fatherClass);
        if (currType instanceof TYPE_FUNCTION) {
            /* Different signatures- overload*/
            if (!overrideMethod.equals(currType)) {
                throw new SemanticException("Overload functions named: %s", this);
            }
            return;
        } else {
            /* There is a variable with the same name in a parent class (and it's not a function) */
            throw new SemanticException("Duplicated definitions named in parent's class", this);
        }

    }
}