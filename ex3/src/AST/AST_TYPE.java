package AST;

public class AST_TYPE extends AST_Node{
    public String type;
    public String idValue;
    public AST_TYPE(String type, String idValue){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== type -> ID(%s)\n", idValue);
        this.type = type;
        this.idValue = idValue;
    }

    public AST_TYPE(String type){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== type -> %s\n", type);
        this.type = type;
    }

    public void PrintMe() {
        System.out.format("AST_TYPE: %s\n", type);
        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
                String.format("type %s\n", type));
    }
}
