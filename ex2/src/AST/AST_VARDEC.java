package AST;

public class AST_VARDEC {
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEWEXP newExp;

    public AST_VARDEC(AST_TYPE type, String name, AST_EXP exp, AST_NEWEXP newExp){
        this.type = type;
        this.name = name;
        this.exp = exp;
        this.newExp = newExp;
    }
}
