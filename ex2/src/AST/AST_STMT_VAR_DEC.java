package AST;

public class AST_STMT_VAR_DEC extends AST_STMT{
    public AST_VAR_DEC var;

    public AST_STMT_VAR_DEC(AST_VAR_DEC var) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        // TODO: Add print derivation rule

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
    }
}
