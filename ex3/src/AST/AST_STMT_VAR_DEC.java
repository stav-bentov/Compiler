package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_VAR_DEC extends AST_STMT{
    public AST_VAR_DEC var;

    public AST_STMT_VAR_DEC(AST_VAR_DEC var) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("====================== stmt -> varDec SEMICOLON }\n");
        this.var = var;
    }

    public void PrintMe() {
        System.out.print("AST_STMT_VAR_DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        var.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "stmt var dec\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        var.SemantMe();
        return null;
    }
}
