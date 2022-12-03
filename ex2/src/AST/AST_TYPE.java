package AST;

public class AST_TYPE extends AST_Node{
    public String type;
    public String idValue;
    public AST_TYPE(String type, String idValue){
        this.type = type;
        this.idValue = idValue;
        SerialNumber = AST_Node_Serial_Number.getFresh();

    }
}
