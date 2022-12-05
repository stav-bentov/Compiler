package AST;

public class AST_VAR_DEC<T extends AST_Node> extends AST_Node{
    public AST_TYPE type;
    public String id;
    public T exp;

    public AST_VAR_DEC(AST_TYPE type, String id, T exp){
        this.type = type;
        this.id = id;
        this.exp = exp;
        SerialNumber = AST_Node_Serial_Number.getFresh();

        if (exp == null)
            System.out.format("====================== varDec -> %s ID(%s)\n", type.type, id);
        else if (exp instanceof AST_EXP)
            System.out.format("====================== varDec -> %s ID(%s) ASSIGN exp\n", type.type, id);
        else if (exp instanceof AST_NEW_EXP)
            System.out.format("====================== varDec -> %s ID(%s) ASSIGN newExp\n", type.type, id);
    }

    public void PrintMe() {
        System.out.format("AST_VAR_DEC<%s>\n", type.type);

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        type.PrintMe();
        if(exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
                String.format("var dec %s %s\n", type.type, id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }
}
