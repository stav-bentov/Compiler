package AST;

public class AST_ARGUMENT extends AST_Node{
    public AST_TYPE type;
    public String id;

    public AST_ARGUMENT(AST_TYPE type, String id){
        this.type = type;
        this.id = id;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== argument -> type(%s) ID(%s)\n", type.type, this.id);
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
}
