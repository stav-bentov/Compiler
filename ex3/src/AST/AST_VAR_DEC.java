package AST;

import TYPES.*;

public class AST_VAR_DEC<T extends AST_Node> extends AST_Node{
    public AST_TYPE type;
    public String id;
    public T exp;

    public AST_VAR_DEC(AST_TYPE type, String id, T exp){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (exp == null)
            System.out.format("====================== varDec -> type(%s) ID(%s)\n", type.type, id);
        else if (exp instanceof AST_EXP)
            System.out.format("====================== varDec -> type(%s) ID(%s) ASSIGN exp\n", type.type, id);
        else if (exp instanceof AST_NEW_EXP)
            System.out.format("====================== varDec -> type(%s) ID(%s) ASSIGN newExp\n", type.type, id);
        this.type = type;
        this.id = id;
        this.exp = exp;
    }

    public void PrintMe() {
        System.out.format("AST_VAR_DEC<%s>\n", type.type);

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        type.PrintMe();
        if(exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
                String.format("var dec %s %s\n", type.type, id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /* Check: 1. No other variable with this name in current scope
                  2. ASSUMPTION!!! No class/ array with this name
                  3. id != "void", "string", "int"
                  4. ASSUMPTION!!! If we are in class-  no variable with this name in parent class
         */
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.id) != null) throw new SemanticException("%s id already declared", this.id);
        if (SYMBOL_TABLE.getInstance().typeCanBeInstanced(this.id) != null) throw new SemanticException("%s is a class/ array/string/int", this.id);
        if (this.id.equals("void")) throw new SemanticException("%s is void", this.id);
        /* If we are not in a function will return false (good)
           If we are in a function will return - null if there is no variable name id
                                                type with name id*/
        if(SYMBOL_TABLE.getInstance().findInInheritance(this.id) != null)
        {
            throw new SemanticException("%s declared in parent class", this.id);
        }

        /* Check: type can be instanced (is in AST_TYPE) */
        /* Compare types if there is an assignment */
        TYPE varInnerType = this.type.SemantMe();
        if (exp != null)
        {
            TYPE expType = exp.SemantMe();
            if (expType instanceof TYPE_NIL)
            {   /* Only variables of arrays and classes can be defined with null expression */
                if (!(varInnerType instanceof TYPE_CLASS) && !(varInnerType instanceof TYPE_ARRAY))
                    throw new SemanticException("Assign types doesnt match (wrong classes)");
            }
            else
            {
                if (!(varInnerType.getClass().equals(expType.getClass())))
                    throw new SemanticException("Assign types doesnt match");
                if (varInnerType instanceof TYPE_CLASS)
                {
                    if (!expType.equals(varInnerType))
                    {
                        /* Make sure expType inherited from varInnerType */
                        if (!expType.inheritsFrom(varInnerType))
                            throw new SemanticException("Assign types doesnt match (wrong classes)");
                    }
                }
                if (varInnerType instanceof TYPE_ARRAY)
                {
                    /* Make sure expType inherited from varInnerType */
                    varInnerType.equals(expType);
                }
            }

        }

        TYPE_VAR currVar = new TYPE_VAR(this.id, varInnerType);
        SYMBOL_TABLE.getInstance().enter(this.id, currVar, false);
        return currVar;
    }
}
