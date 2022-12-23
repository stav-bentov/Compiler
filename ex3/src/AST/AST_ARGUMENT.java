package AST;

import TYPES.TYPE;

public class AST_ARGUMENT extends AST_Node{
    public AST_TYPE type;
    public String id;

    public AST_ARGUMENT(AST_TYPE type, String id){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== argument -> type(%s) ID(%s)\n", type.type, id);
        this.type = type;
        this.id = id;
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST_ARGUMENT\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(type != null) type.PrintMe();
        System.out.format("ID( %s )\n",this.id);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("argument: %s",this.id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        TYPE arg_type = this.type.SemantMe();
        /* Check parameter name (=id) is not class name, "int", "string" or array name *and* void*/
        if (SYMBOL_TABLE.getInstance().typeCanBeInstanced(this.id) || this.id.equals("void") )
            throw new SemanticException("invalid parameter name!");
        return arg_type;
    }
}
