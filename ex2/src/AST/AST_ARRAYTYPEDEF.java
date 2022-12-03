package AST;

public class AST_ARRAYTYPEDEF extends AST_Node{
    public String name;
    public AST_TYPE type;

    public AST_ARRAYTYPEDEF(String name, AST_TYPE type){
        this.name = name;
        this.type = type;
        SerialNumber = AST_Node_Serial_Number.getFresh();

    }
}
