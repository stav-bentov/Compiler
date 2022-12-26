package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;

public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> l;

    public AST_STMT_ID(AST_VAR var, String id, AST_LIST<AST_EXP> l) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (var != null && l != null)
            System.out.printf("====================== stmt -> var DOT ID(%s)(commaExpList) SEMICOLON", id);
        else if (var != null && l == null)
            System.out.printf("====================== stmt -> var DOT ID(%s)() SEMICOLON", id);
        else if (var == null && l != null)
            System.out.printf("====================== stmt -> ID(%s)(commaExpList) SEMICOLON", id);
        else
            System.out.printf("====================== stmt ->  ID(%s)() SEMICOLON", id);
        this.var = var;
        this.id = id;
        this.l = l;
    }

    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.format("AST_STMT_ID %s\n", id);

        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (var != null) var.PrintMe();
        if (l != null) l.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ASSIGN\nID(%s) := right\n", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if(l != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        TYPE typeFound;

        /* Find type */
        if (var == null) {
            typeFound = SYMBOL_TABLE.getInstance().findInInheritance(this.id);
        }
        else {
            TYPE typeOfVar = var.SemantMe().type;
            if (!typeOfVar.isClass()) {
                throw new SemanticException(
                        "This type is not a class and therefore does not have class methods",
                        this
                );
            }
            TYPE_CLASS typeClassOfVar = (TYPE_CLASS)typeOfVar;
            typeFound = SYMBOL_TABLE.getInstance().findInInheritance(this.id, typeClassOfVar);
        }

        /* Check if type was found */
        if (typeFound == null) {
            /* If not found, and it can be a global function, check in global scope */
            if (var == null) { // If var is null it can be a global function
                typeFound = findInGlobalScope(this.id);
            }

            /* Check if it is still not found */
            if (typeFound == null) {
                /* Now it really does not exist */
                throw new SemanticException(
                        "Function does not exist",
                        this
                );
            }
        }

        /* Check if it's a method */
        if (!(typeFound instanceof TYPE_FUNCTION)) {
            // There is no way a function with this name exists,
            // because this type wouldn't have been inserted to this symbol table in the first place
            throw new SemanticException(
                    "This function does not exist in an open scope",
                    this
            );
        }

        /* Check call */
        CallToFuncMatchesFunc((TYPE_FUNCTION)typeFound); // We've already made sure typeFound is of TYPE_FUNC

        return null;
    }

    private void CallToFuncMatchesFunc(TYPE_FUNCTION func) {
        /* Check if parameters match expected parameters */
        TYPE_LIST params = (TYPE_LIST)l.SemantMe(); // l.SemantMe() supposed to return TYPE_LIST
        if (!func.params.equalsForValidatingGivenParams(params)) {
            throw new SemanticException(
                    "Parameters received do not match expected parameters",
                    this
            );
        }

        /* No need to check return type - no assignment is done here */
    }
}
