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
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.id) != null) throw new SemanticException("%s id already declared");
        /* TODO: check if the name can be instanced or void  is it right or wrong? I dont understand
        if (SYMBOL_TABLE.getInstance().typeCanBeInstanced(this.id) != null) throw new SemanticException("%s is a class/ array/string/int");
        if (this.id.equals("void")) throw new SemanticException("%s is void");*/

        /* If we are not in a class check there is no variable (CFIELD) with this name in parents classes */
        if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS){
            if (SYMBOL_TABLE.getInstance().findInInheritance(this.id) != null) {
                throw new SemanticException("%s declared in parent class");
            }
        }

        /* Check: type can be instanced (is in AST_TYPE) */
        /* Compare types if there is an assignment
        *  2 options: 1. assign Class / Class variable
        *             2. assign Array
        *             3. assign int
        *             4. assign string
        * BUT- if it's vardec in CFIELD(we are in a class), can only assign string, int, null*/
        TYPE typeToAssign = this.type.SemantMe();
        if (this.exp != null)
        {
            TYPE expType = this.exp.SemantMe();
            /* vardec is CFIELD- just const string/int/null assignments*/
            if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS)
            {
                if (!(this.exp instanceof AST_EXP_OPT))
                    throw new SemanticException("Data member inside a class can be initialized only with a constant value");
                if (expType instanceof TYPE_NIL)
                {
                    if (!(typeToAssign instanceof TYPE_CLASS || typeToAssign instanceof TYPE_ARRAY))
                        throw new SemanticException("Can assign null only on arrays and classes");
                }
            }
            else
            { /* We are inside a function/ method/ global scope/ if/ while*/
                /* Assumption new_exp can't be from type null*/
                if (expType instanceof TYPE_NIL)
                {
                    /* Only variables of arrays and classes can be defined with null expression */
                    if (!(typeToAssign instanceof TYPE_CLASS) && !(typeToAssign instanceof TYPE_ARRAY))
                        throw new SemanticException("Assign types doesnt match (wrong classes)");
                    /* TODO: Update array size to 0?*/
                }
                else
                {
                    if (typeToAssign instanceof TYPE_ARRAY)
                    {
                        /* check if it's an array of arrays/ arrays of classes and if it's match*/
                        if(!compareTypeArrays((TYPE_ARRAY) typeToAssign, expType))
                        {
                            throw new SemanticException("Assign types doesnt match (wrong classes)");
                        }

                        if (this.exp instanceof AST_NEW_EXP)
                        {/* a size of an array is given*/
                            /*TODO: should I take care of that??*/
                        }
                    }
                    else if (typeToAssign instanceof TYPE_CLASS)
                    {
                        if (!typeToAssign.equals(expType))
                        {
                            /* Make sure expType inherited from typeToAssign */
                            if (!((TYPE_CLASS) expType).inheritsFrom(typeToAssign))
                                throw new SemanticException("Assign types doesnt match (wrong classes)");
                        }
                    }
                    else
                    {
                        /* Cases of int/ string*/
                        if(!(typeToAssign.getClass().equals(expType.getClass())))
                        {
                            throw new SemanticException("Assign types doesnt match (wrong classes)");
                        }
                    }
                }
            }
        }

        TYPE_VAR currVar = new TYPE_VAR(this.id, typeToAssign);
        SYMBOL_TABLE.getInstance().enter(this.id, currVar, false);
        return currVar;
    }

    /* Receives the array that the assignment is made on c
       Check the types of the expression that is assigned*/
    public boolean compareTypeArrays(TYPE_ARRAY arrayToAssign, TYPE assignedType)
    {
        TYPE requiredType = arrayToAssign.arrayType;
        if (!(requiredType.getClass().equals(assignedType.getClass())))
            return false;
        if (requiredType instanceof TYPE_CLASS)
        {
            /*Check inheritance*/
            if (!requiredType.equals(assignedType))
            {
                /* Make sure assignedType inherited from requiredType */
                if (!((TYPE_CLASS) assignedType).inheritsFrom(requiredType))
                    return false;
            }
        }
        return true;
    }
}

