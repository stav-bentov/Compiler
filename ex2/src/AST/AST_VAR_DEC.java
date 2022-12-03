package AST;

public class AST_VAR_DEC extends AST_Node{
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEWEXP newExp;

    public AST_VAR_DEC(AST_TYPE type, String name){
        this.type = type;
        this.name = name;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== varDec -> %s ID(%s)\n", type.type, name);
    }

    public AST_VAR_DEC(AST_TYPE type, String name, AST_EXP exp){
        this.type = type;
        this.name = name;
        this.exp = exp;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== varDec -> %s ID(%s) := exp\n", type.type, name);
    }

    public AST_VAR_DEC(AST_TYPE type, String name, AST_EXP exp, AST_NEWEXP newExp){
        this.type = type;
        this.name = name;
        this.exp = exp;
        this.newExp = newExp;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== varDec -> %s ID(%s) := newExp\n", type.type, name);
    }
}
