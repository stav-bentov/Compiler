package AST;

public class AST_ARGUMENT extends AST_Node{
    public AST_TYPE type;
    public String name;

    public AST_ARGUMENT(AST_TYPE type, String name){
        this.type = type;
        this.name = name;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== argument -> type(%s) ID(%s)\n", type.type, this.name);
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
        System.out.format("ID( %s )\n",this.name);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("argument: %s",this.name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }
}
