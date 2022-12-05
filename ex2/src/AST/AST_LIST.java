package AST;

public class AST_LIST<T extends AST_Node> extends AST_Node{
    public String type;
    public T head;
    public AST_LIST<T> tail;

    public AST_LIST(T head, AST_LIST<T> tail) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        if (head instanceof AST_DEC) type = "AST_DEC";
        if (head instanceof  AST_ARGUMENT) type = "AST_ARGUMENT";
        if (head instanceof AST_CFIELD ) type = "AST_CFIELD";
        if (head instanceof AST_STMT ) type = "AST_STMT";
        if (head instanceof AST_EXP ) type = "AST_EXP";

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
        System.out.format("====================== AST_LIST -> of type(%s)\n", type);
    }

    public void PrintMe() {
        System.out.format("AST_LIST<%s>\n", type);

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        head.PrintMe();
        if (tail != null) tail.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST_LIST<%s>:\n", type));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
    }
}
