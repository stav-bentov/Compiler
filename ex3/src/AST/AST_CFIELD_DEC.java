package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;
    public String type;

    public AST_CFIELD_DEC(T dec) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (dec instanceof AST_VAR_DEC) {
            this.type = "varDec";
        }
        else if (dec instanceof AST_FUNC_DEC) {
            this.type = "funcDec";
        }
        System.out.format("====================== cField -> %s\n", this.type);
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

    public TYPE SemantMe() throws SemanticException {
        return dec.SemantMe();
    }
}
