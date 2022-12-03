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
}
