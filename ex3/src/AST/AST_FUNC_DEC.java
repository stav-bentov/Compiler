package AST;
import TYPES.*;

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
        boolean is_in_scope, is_valid_method, is_valid_function;
        TYPE_FUNCTION curr_type_func;
        ScopeTypeEnum scope_type;
        TYPE_LIST params = null;

        /* Search the type of the function- (class) method or (global) function */
        scope_type = SYMBOL_TABLE.getInstance().getCurrentScopeType();
        if (scope_type != ScopeTypeEnum.CLASS && scopeType != ScopeTypeEnum.GLOBAL)
        {
            throw new SemanticException("Function: %s is not declared in a Global scope or in a Class scope", name);
        }

        /*  CHECK: check if there are classes/ arrays with func name and func name != "string","int","void"
            case (class) method: check if there are previous declarations with this name in current class and
                                 if there is in a parent class- make sure it has the same signature
            case (global) function: check if there are previous declarations with this name in global scope
         */
        /* ASSUMPTION!! method name can't be a class/ array/ "string"/ "void"/ "int*/
        is_in_scope = SYMBOL_TABLE.getInstance().findInLastScope(this.name) != null && !SYMBOL_TABLE.getInstance().typeCanBeInstanced(this.name) && !this.name.equals("void");
        is_valid_method = scope_type == ScopeTypeEnum.CLASS && !is_in_scope;
        is_valid_function = scope_type ==  ScopeTypeEnum.GLOBAL && !is_in_scope;

        if (is_valid_method || is_valid_function)
        {
            /*  SemantMe will throw an error if the return type is invalid */
            curr_type_func = new TYPE_FUNCTION(this.return_type.SemantMe(), this.name, null);
            SYMBOL_TABLE.getInstance().enter(name, curr_type_func);
            SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.FUNC, curr_type_func);

            /* SemantMe() checks parameters:
               1. parameter's type can be instanced (only a "string"/ "int"/ previous declared class/ previous declared array/"void")
               2. parameter's name is not string/ int/ previous declared class/ previous declared array/"void"*/
            if (argList != null)
            {
                params = (TYPE_LIST) argList.SemantMe();
            }

            curr_type_func.params = params;

            /* Make sure there is only an override and no overload or using with function's name */
            if (is_valid_method)
                isValidMethod(name, curr_type_func);

            /* If the return type isn't match or if there is a semantic error inside the scope- SemantMe() will throw an error*/
            if (stmtList != null)
                stmtList.SemantMe();

            SYMBOL_TABLE.getInstance().endScope();
            return curr_type_func;
        }
        throw new SemanticException("Duplicated definitions named: %s", name);
    }

    public void isValidMethod(String name, TYPE_FUNCTION override_method)
    {
        TYPE_CLASS curr_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().getCurrentClass();
        /* Check if there is a variable named "name" or a function overloading*/
        while (curr_class != null)
        {
            if (curr_class.data_members != null) {
                TYPE_LIST list_pointer = curr_class.data_members;
                while (list_pointer.head != null) {
                    if (list_pointer.head.name.equals(name)) {
                        if (list_pointer.head instanceof TYPE_FUNCTION) {
                            /* Different signatures- overload*/
                            if (!override_method.equals(list_pointer.head)) {
                                throw new SemanticException("Overload functions named: %s", name);
                            }
                            return;
                        } else {
                            /* There is a variable with the same name in a parent class */
                            throw new SemanticException("Duplicated definitions named in parent's class");
                        }
                    }
                    list_pointer = list_pointer.tail;
                }
            }
            curr_class = curr_class.father;
        }
    }
}