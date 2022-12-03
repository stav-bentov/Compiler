package AST;

public class AST_VAR_DEC<T extends AST_Node> extends AST_Node{
    public AST_TYPE type;
    public String name;
    public T exp;

    public AST_VAR_DEC(AST_TYPE type, String name){
        this.type = type;
        this.name = name;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== varDec -> %s ID(%s)\n", type.type, name);
    }

    public AST_VAR_DEC(AST_TYPE type, String name, T exp){
        this.type = type;
        this.name = name;
        this.exp = exp;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== varDec -> %s ID(%s) := exp\n", type.type, name);
    }
}
