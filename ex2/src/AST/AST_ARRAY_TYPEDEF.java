package AST;

public class AST_ARRAY_TYPEDEF extends AST_Node{
    public String name;
    public AST_TYPE type;

    public AST_ARRAY_TYPEDEF(String name, AST_TYPE type){
        this.name = name;
        this.type = type;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== array_typedef -> %s %s\n", type.type, this.name);
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
}
