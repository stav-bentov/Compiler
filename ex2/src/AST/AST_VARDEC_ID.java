package AST;

public class AST_VARDEC_ID extends AST_VARDEC{
    public AST_TYPE type;
    public String name;

    public AST_VARDEC_ID(AST_TYPE type, String name){
        this.type = type;
        this.name = name;
    }
}
