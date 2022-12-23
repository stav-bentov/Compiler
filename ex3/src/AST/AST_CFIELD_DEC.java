package AST;

import TYPES.TYPE;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;

    public AST_CFIELD_DEC(T dec) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        String type = "";
        if (dec instanceof AST_VAR_DEC) {
            type = "varDec";
        }
        else if (dec instanceof AST_FUNC_DEC) {
            type = "funcDec";
        }
        System.out.format("====================== cField -> %s\n", type);
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

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "cfield_dec");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
    }

    public TYPE SemantMe() {
        return dec.SemantMe();
    }
}
