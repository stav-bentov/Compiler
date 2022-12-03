package AST;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;

    public AST_CFIELD_DEC(T dec) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        // TODO: Add print derivation rule

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.dec = dec;
    }
}
