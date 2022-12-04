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
}
