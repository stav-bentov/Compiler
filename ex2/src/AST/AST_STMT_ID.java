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
}
