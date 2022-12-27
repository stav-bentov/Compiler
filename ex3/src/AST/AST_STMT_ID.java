package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> l;

    public AST_STMT_ID(AST_VAR var, String id, AST_LIST<AST_EXP> l, int line) {
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
        this.line = line;
    }

    public void PrintMe()
    {
        System.out.format("stmt id %s\n", id);

        if (var != null)
            var.PrintMe();
        if (l != null)
            l.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("stmt id %s\n", id));
        if(var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if(l != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        TYPE typeFound;

        /* Find type */
        if (var == null) {
            typeFound = SYMBOL_TABLE.getInstance().findInInheritance(this.id);
        }
        else {
            TYPE typeOfVar = ((TYPE_VAR)var.SemantMe()).type;
            if (!typeOfVar.isClass()) {
                throw new SemanticException(this);
            }
            TYPE_CLASS typeClassOfVar = (TYPE_CLASS)typeOfVar;
            typeFound = SYMBOL_TABLE.getInstance().findInInheritance(this.id, typeClassOfVar);
        }

        /* Check if type was found */
        if (typeFound == null) {
            /* If not found, and it can be a global function, check in global scope */
            if (var == null) { // If var is null it can be a global function
                typeFound = SYMBOL_TABLE.getInstance().findInGlobal(this.id);
            }

            /* Check if it is still not found */
            if (typeFound == null) {
                /* Now it really does not exist */
                throw new SemanticException(this);
            }
        }

        /* Check if it's a method */
        if (!(typeFound instanceof TYPE_FUNCTION)) {
            // There is no way a function with this name exists,
            // because this type wouldn't have been inserted to this symbol table in the first place
            throw new SemanticException(this);
        }

        /* Check call */
        CallToFuncMatchesFunc((TYPE_FUNCTION)typeFound); // We've already made sure typeFound is of TYPE_FUNC

        return null;
    }

    //appears in both AST_STMT_ID and AST_EXP_ID
    private void CallToFuncMatchesFunc(TYPE_FUNCTION func) throws SemanticException{
        //if our func has no params and the called func has params - throw error
        if((this.l == null && func.params != null) || (func.params == null && this.l != null)){
            throw new SemanticException(this);
        }

        //if they are both null all is good darling
        if(this.l != null && func.params != null){
            /* Check if parameters match expected parameters */
            TYPE_LIST params = (TYPE_LIST) this.l.SemantMe(); // l.SemantMe() supposed to return TYPE_LIST
            if (!func.params.equalsForValidatingGivenParams(params)) {
                throw new SemanticException(this);
            }
        }
    }
}
