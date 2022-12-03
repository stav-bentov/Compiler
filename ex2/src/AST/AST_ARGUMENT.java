package AST;

public class AST_ARGUMENT extends AST_Node{
    public AST_TYPE type;
    public String name;

    public AST_ARGUMENT(AST_TYPE type, String name){
        this.type = type;
        this.name = name;
    }
}
