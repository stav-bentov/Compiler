package AST;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_VAR_DEC extends AST_STMT{
    public AST_VAR_DEC var;

    public AST_STMT_VAR_DEC(AST_VAR_DEC var, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("====================== stmt -> varDec SEMICOLON }\n");
        this.var = var;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("stmt varDec\n");

        if (var != null)
            var.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "stmt varDec\n");
        if (var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        var.SemantMe();
        return null;
    }

    @Override
    public TEMP IRme()
    {
        return this.var.IRme();
    }
}
