package AST;

public class AST_PROGRAM extends AST_Node{
    public AST_LIST<AST_DEC> decList;

    public AST_PROGRAM(AST_LIST<AST_DEC> decList){
        this.decList = decList;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }
}
