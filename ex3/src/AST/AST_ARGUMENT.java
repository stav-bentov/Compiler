package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARGUMENT extends AST_Node{
    public AST_TYPE type;
    public String id;

    public AST_ARGUMENT(AST_TYPE type, String id, int line){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== argument -> type(%s) ID(%s)\n", type.type, id);
        this.type = type;
        this.id = id;
        this.line = line;
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST_ARGUMENT\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(type != null) type.PrintMe();
        System.out.format("ID( %s )\n",this.id);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("argument: %s",this.id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /* Checking that a variable can be created from type made in SemantMe() */
        TYPE_VAR varType = new TYPE_VAR(this.id, this.type.SemantMe());
        /* Assumption: the arguments are the first to get in the symbol table then if there is a duplicate name- will find it*/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.id) != null)
        {
            throw new SemanticException(this);
        }
        /* argument type can't be TYPE_VOID!*/
        if (varType.type instanceof TYPE_VOID)
        {
            throw new SemanticException(this);
        }
        SYMBOL_TABLE.getInstance().enter(this.id, varType, false);
        return varType;
    }
}
