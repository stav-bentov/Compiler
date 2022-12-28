package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> l;

    public AST_STMT_ID(AST_VAR var, String id, AST_LIST<AST_EXP> l, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (var != null && l != null)
            System.out.printf("====================== stmt -> var DOT ID(%s)(commaExpList) SEMICOLON", id);
        else if (var != null && l == null)
            System.out.printf("====================== stmt -> var DOT ID(%s)() SEMICOLON", id);
        else if (var == null && l != null)
            System.out.printf("====================== stmt -> ID(%s)(commaExpList) SEMICOLON", id);
        else
            System.out.printf("====================== stmt ->  ID(%s)() SEMICOLON", id);
        this.var = var;
        this.id = id;
        this.l = l;
        this.line = line;
    }

    public void PrintMe()
    {
        System.out.format("stmt id %s\n", id);

        if (var != null)
            var.PrintMe();
        if (l != null)
            l.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("stmt id %s\n", id));
        if(var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if(l != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        return CheckCallToFunc(id, var, l);
    }
}
