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

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST_CFIELD_DEC\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(dec != null) dec.PrintMe();
        System.out.format("CFIELD_DEC\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "cfield_dec");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
    }
}
