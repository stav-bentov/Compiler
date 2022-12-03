package AST;

public class AST_STMT_ID_VAR_EXP extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> l;

    public AST_STMT_ID_VAR_EXP(AST_VAR var, String id, AST_LIST<AST_EXP> l) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        // TODO: Add print derivation rule

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id = id;
        this.l = l;
    }
}
