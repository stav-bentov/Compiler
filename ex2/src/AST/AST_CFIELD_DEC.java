package AST;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;

    public AST_CFIELD_DEC(T dec) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        String type = "";
        if (dec instanceof AST_VAR_DEC) {
            type = "varDec";
        }
        else if (dec instanceof AST_FUNC_DEC) {
            type = "funcDec";
        }

        System.out.format("====================== cField -> %s\n", type);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.dec = dec;
    }
}
