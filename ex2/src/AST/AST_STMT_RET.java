package AST;

public class AST_STMT_RET extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RET(AST_EXP exp) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        String addedExp = "";
        if (exp != null) {
            addedExp = " exp";
        }

        String deriveRule = String.format("====================== stmt -> RETURN%s SEMICOLON\n", addedExp);
        System.out.print(deriveRule);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    public void PrintMe() {
        System.out.print("AST_LIST");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "return stmt\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }
}
