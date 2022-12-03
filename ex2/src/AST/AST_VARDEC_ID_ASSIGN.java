package AST;

public class AST_VARDEC_ID_ASSIGN extends AST_VARDEC{
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;

    public AST_VARDEC_ID_ASSIGN(AST_TYPE type, String name, AST_EXP exp){
        this.type = type;
        this.name = name;
        this.exp = exp;
    }
}
