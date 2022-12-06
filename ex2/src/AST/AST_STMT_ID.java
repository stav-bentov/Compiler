package AST;

public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> l;

    public AST_STMT_ID(AST_VAR var, String id, AST_LIST<AST_EXP> l) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (var != null && l != null)
            System.out.printf("====================== stmt ->  [ var DOT ] ID(%s)([ exp [ COMMA exp ]*]) SEMICOLON", id);
        else if (var != null)
            System.out.printf("====================== stmt ->  [ var DOT ] ID(%s)() SEMICOLON", id);
        else if (l != null)
            System.out.printf("====================== stmt ->  [ var DOT ] ID(%s)([ exp [ COMMA exp ]*]) SEMICOLON", id);
        else
            System.out.printf("====================== stmt ->  ID(%s)() SEMICOLON", id);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id = id;
        this.l = l;
    }

    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.format("AST_STMT_ID %s\n", id);

        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (var != null) var.PrintMe();
        if (l != null) l.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ASSIGN\nID(%s) := right\n", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if(l != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
    }
}
