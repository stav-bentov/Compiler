package AST;
import TYPES.*;

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

    public TYPE SemantMe() throws SemanticException
    {
        TYPE head_type = null;
        TYPE_LIST tail_type = null;

        /* function/method's parameters list
         check parameters:
               1. (checked in AST_TYPE) parameter's type can be instanced (only a "string"/ "int"/ previous declared class/ previous declared array)
               2. (checked here) parameter's name is not string/ int/ previous declared class/ previous declared array*/
        if (head instanceof AST_ARGUMENT)
        {
            head_type = this.head.SemantMe();
            /* Assumption: the arguments are the first to get in the symbol table then if there is a duplicate name- will find it*/
            if (SYMBOL_TABLE.getInstance().findInLastScope(((AST_ARGUMENT)this.head).id))
            {
                throw new SemanticException("There are at least 2 parameters named: %s", ((AST_ARGUMENT)this.head).id);
            }
            /* Parameter's type and name are valid*/
            SYMBOL_TABLE.getInstance().enter(head_type.name, head_type, false);
            tail_type = (this.tail == null) ? null : (TYPE_LIST) this.tail.SemantMe();
        }

        if (head instanceof AST_STMT)
        {
            TYPE func_type = ;
            head_type = this.head.SemantMe();
            if (this.head instanceof AST_STMT_RET)
            {
                /* Check if the function return type is same as head_type */
            }
            /* Parameter's type and name are valid*/
            SYMBOL_TABLE.getInstance().enter(head_type.name, head_type, false);
            tail_type = (this.tail == null) ? null : (TYPE_LIST) this.tail.SemantMe();
        }

        return new TYPE_LIST(head_type, tail_type);
    }
}
