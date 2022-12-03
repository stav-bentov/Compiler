package AST;

public class AST_STMT_VAR_DEC extends AST_STMT{
    public AST_VAR_DEC var;

    public AST_STMT_VAR_DEC(AST_VAR_DEC var) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> varDec SEMICOLON }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
    }
}
