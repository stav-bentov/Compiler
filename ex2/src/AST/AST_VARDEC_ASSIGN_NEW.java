package AST;

public class AST_VARDEC_ASSIGN_NEW extends AST_VARDEC{
    public AST_TYPE type;
    public String name;
    public AST_NEWEXP newExp;

    public AST_VARDEC_ASSIGN_NEW(AST_TYPE type, String name, AST_NEWEXP newExp){
        this.type = type;
        this.name = name;
        this.newExp = newExp;
    }
}
