package TEMP;

public class TEMP_LIST {
    public TEMP head;
    public TEMP_LIST tail;
    public int len = 0;

    public TEMP_LIST(TEMP head, TEMP_LIST tail)
    {
        this.head = head;
        this.tail = tail;
        this.len = 1;
    }
}
