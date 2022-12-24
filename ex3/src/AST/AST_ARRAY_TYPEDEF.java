package AST;

import TYPES.*;

public class AST_ARRAY_TYPEDEF extends AST_Node{
    public String name;
    public AST_TYPE type;

    public AST_ARRAY_TYPEDEF(String name, AST_TYPE type){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("===================== arrayTypedef -> ARRAY ID(%s) ASSIGN type(%s) []\n", name, type.type);
        this.name = name;
        this.type = type;
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST ARRAY_TYPEDEF\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(type != null) type.PrintMe();
        System.out.format("ID( %s )\n",this.name);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("array_typedef: %s",this.name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /* I'm not sure it's necessary (because I think that cup defines it to only be declared in Global scope */
        if (SYMBOL_TABLE.getInstance().getCurrentScopeType() != ScopeTypeEnum.GLOBAL) throw new SemanticException("Array can only be declared in Global scope");
        /* There is no variable/ class/ function/ with this name/ "string"/"void"/"int"*/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.name) != null) throw new SemanticException("id already declared");
        /* Check: type can be instanced (is in AST_TYPE) if this is a defined class/ array/ string/ int */
        TYPE arrayType = this.type.SemantMe();
        TYPE_ARRAY currArray = new TYPE_ARRAY(arrayType, this.name);
        /* REMEMBER: Array can be instanced*/
        SYMBOL_TABLE.getInstance().enter(this.name, currArray, true);
        return null;
    }
}
