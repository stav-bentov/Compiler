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
        /* No need to create TYPE_LIST*/
        if (head instanceof AST_STMT || head instanceof AST_DEC)
        {
            this.head.SemantMe();
            if (this.tail != null)
                this.tail.SemantMe();
            return null;
        }

        TYPE headType = null;
        TYPE_LIST tailType = null;

        /* AST_CFIELD (TYPE_LIST of TYPE_VAR and TYPE_FUNCTION)
           AST_EXP (TYPE_LIST of TYPE)
           AST_ARGUMENT (TYPE_LIST of TYPE_ARGUMENT)*/
        /* CASE AST_ARGUMENT: parameters list check parameters:
                              1. (checked in AST_TYPE) parameter's type can be instanced (only a "string"/ "int"/ previous declared
                                  class/ previous declared array)
                              2. (checked here) parameter's name is not string/ int/ previous declared class/ previous declared array*/
        /* CASE AST_EXP TODO: ask Rotem if it's OK There will be a need in this list to compare functions/ classes?*/
        /* CASE AST_STMT Assumption (TODO) for Lilach - SemantMe on AST_STMT_RET will check return type.. also- Lilach making the enters if needed*/
        headType = this.head.SemantMe();
        SYMBOL_TABLE.getInstance().enter(headType.name, headType, false);
        tailType = (this.tail == null) ? null : (TYPE_LIST) this.tail.SemantMe();

        return new TYPE_LIST(headType, tailType);
    }
}
