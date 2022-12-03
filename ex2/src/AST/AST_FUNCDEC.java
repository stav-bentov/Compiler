package AST;

public class AST_FUNCDEC extends AST_Node{
    public AST_TYPE type;
    public String name;
    public AST_LIST<AST_ARGUMENT> argList;
    public AST_LIST<AST_STMT> stmtList;

    public AST_FUNCDEC(AST_TYPE type, String name, AST_LIST<AST_ARGUMENT> argList, AST_LIST<AST_STMT> stmtList){
        this.type = type;
        this.name = name;
        this.argList = argList;
        this.stmtList = stmtList;
        SerialNumber = AST_Node_Serial_Number.getFresh();

    }


}
