package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;
    public String type;

    public AST_CFIELD_DEC(T dec, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (dec instanceof AST_VAR_DEC) {
            this.type = "varDec";
        }
        else if (dec instanceof AST_FUNC_DEC) {
            this.type = "funcDec";
        }
        System.out.format("====================== cField -> %s\n", this.type);
        this.dec = dec;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("cfield: %s\n", this.type);

        if(dec != null)
            dec.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("cfield: %s", type));
        if (dec != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        return dec.SemantMe();
    }
}
