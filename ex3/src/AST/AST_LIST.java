package AST;

public class AST_LIST<T extends AST_Node> extends AST_Node{
    public String type;
    public T head;
    public AST_LIST<T> tail;

    public AST_LIST(T head, AST_LIST<T> tail) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (head instanceof AST_DEC) type = "AST_DEC";
        if (head instanceof  AST_ARGUMENT) type = "AST_ARGUMENT";
        if (head instanceof AST_CFIELD ) type = "AST_CFIELD";
        if (head instanceof AST_STMT ) type = "AST_STMT";
        if (head instanceof AST_EXP ) type = "AST_EXP";
        System.out.format("====================== AST_LIST -> of type(%s)\n", type);
        this.head = head;
        this.tail = tail;
    }

    public void PrintMe() {
        System.out.format("AST_LIST<%s>\n", type);

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        head.PrintMe();
        if (tail != null) tail.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST_LIST<%s>:\n", type));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
    }

    public TYPE SemantMe(){
        TYPE head_type = head.SemantMe();

        TYPE_LIST tail_type = null;
        head_type.len = 1;

        if (tail != null){
            tail_type = tail.SemantMe();
            head_type.len = tail_type.len + 1;
        }

        string name;
        switch (head.type){
            case "AST_DEC":
                name = GetAstDecName(head);
                break;

            case "AST_ARGUMENT":
                name = head.id;
                break;

            case "AST_CFIELD":
                name = GetAstCfieldName(head);
                break;

            case "AST_STMT": //TODO: check if ok that is empty
            case "AST_EXP": //TODO: check if ok that is empty
        }

        TYPE_LIST type_list = new TYPE_LIST(head_type, tail_type, name);

        return head_type;
    }

    public String GetAstDecName(AST_DEC head){
        String name;
        switch (head.type){
            case AST_ARRAY_TYPEDEF:
                name = head.name;
                break;

            case AST_CLASS_DEC:
                name = head.className;
                break;

            case AST_FUNC_DEC:
                name = head.name;
                break;

            case AST_VAR_DEC:
                name = head.id;
                break;
        }

        return name;
    }

    public String GetAstCfieldName(AST_CFIELD head){
        String name;
        switch((AST_CFIELD_DEC head).type){
            case "varDec":
                name = (AST_VAR_DEC (AST_CFIELD_DEC head).dec).id;
                break;

            case "funcDec":
                name = (AST_FUNC_DEC (AST_CFIELD_DEC head).dec).name;
                break;
        }

        return name;
    }
}
