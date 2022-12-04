package AST;

public class AST_LIST<T extends AST_Node> extends AST_Node{
    public T head;
    public AST_LIST<T> tail;

    public AST_LIST(T head, AST_LIST<T> tail) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
        System.out.format("====================== AST_LIST -> head: %s\n", head.toString());
    }

    public void PrintMe() {
        System.out.format("AST_LIST<%s>", head.toString());

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
                String.format("AST_LIST<%s>:\n", tail.toString()));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
    }
}
