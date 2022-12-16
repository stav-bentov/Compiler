package AST;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE type;
    public String name;
    public AST_LIST<AST_ARGUMENT> argList;
    public AST_LIST<AST_STMT> stmtList;

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_STMT> stmtList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)() {stmtList}\n", type.type, name);
        this.type = type;
        this.name = name;
        this.stmtList = stmtList;
    }

    public AST_FUNC_DEC(AST_TYPE type, String name, AST_LIST<AST_ARGUMENT> argList, AST_LIST<AST_STMT> stmtList) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== funcDec -> type(%s) ID(%s)(argumentsList) {stmtList}\n", type.type, name);
        this.type = type;
        this.name = name;
        this.argList = argList;
        this.stmtList = stmtList;
    }

    public void PrintMe() {
        System.out.print("AST_FUNC_DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        type.PrintMe();
        if (argList != null) argList.PrintMe();
        stmtList.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("func_dec: %s\n", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (argList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argList.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmtList.SerialNumber);
    }
}
