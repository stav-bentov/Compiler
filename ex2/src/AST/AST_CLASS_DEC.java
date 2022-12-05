package AST;

public class AST_CLASS_DEC extends AST_Node{
    public String className;
    public String extendsName;
    public AST_LIST<AST_CFIELD> cFieldList;

    public AST_CLASS_DEC(String className, String extendsName, AST_LIST<AST_CFIELD> cFieldList){
        this.className = className;
        this.extendsName = extendsName;
        this.cFieldList = cFieldList;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) extends %s cFieldList\n", className, extendsName);
    }

    public AST_CLASS_DEC(String className, AST_LIST<AST_CFIELD> cFieldList){
        this.className = className;
        this.cFieldList = cFieldList;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) cFieldList\n", className, extendsName);
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST_CLASS_DEC\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(cFieldList != null) cFieldList.PrintMe();
        if (extendsName == null) System.out.format("AST_CLASS_DEC %s\n", className);
        else System.out.format("AST_CLASS_DEC %s extends %s\n", className, extendsName);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        String nodeName = extendsName == null ?
                String.format("class_dec %s\n", className) : String.format("class_dec %s extends %s\n", className, extendsName);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, nodeName);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cFieldList.SerialNumber);
    }
}
