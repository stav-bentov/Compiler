package AST;

public class AST_CLASSDEC extends AST_Node{
    public String className;
    public String extendsName;
    public AST_LIST<AST_CFIELD> cFieldList;

    public AST_CLASSDEC(String className, String extendsName, AST_LIST<AST_CFIELD> cFieldList){
        this.className = className;
        this.extendsName = extendsName;
        this.cFieldList = cFieldList;
    }

}
