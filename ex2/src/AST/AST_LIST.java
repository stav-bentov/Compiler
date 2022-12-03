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
    }
}
