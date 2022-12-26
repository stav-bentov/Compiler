package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARRAY_TYPEDEF extends AST_Node{
    public String name;
    public AST_TYPE type;

    public AST_ARRAY_TYPEDEF(String name, AST_TYPE type, int line){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== arrayTypedef -> ARRAY ID(%s) ASSIGN type(%s) []\n", name, type.type);
        this.name = name;
        this.type = type;
        this.line = line;
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST ARRAY_TYPEDEF\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(type != null) type.PrintMe();
        System.out.format("ID( %s )\n",this.name);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("array_typedef: %s",this.name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /* There is no variable/ class/ function/ with this name/ "string"/"void"/"int"*/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.name) != null)
            throw new SemanticException(this);

        /* Check: type can be instanced (is in AST_TYPE) if this is a defined class/ array/ string/ int but also void */
        TYPE arrayType = this.type.SemantMe();
        if (arrayType instanceof TYPE_VOID)
            throw new SemanticException(this);

        TYPE_ARRAY currArray = new TYPE_ARRAY(arrayType, this.name);

        /* REMEMBER: Array can be instanced*/
        SYMBOL_TABLE.getInstance().enter(this.name, currArray, true);
        return null;
    }
}
