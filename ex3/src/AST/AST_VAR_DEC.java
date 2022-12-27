package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC<T extends AST_Node> extends AST_Node{
    public AST_TYPE type;
    public String id;
    public T exp;

    public AST_VAR_DEC(AST_TYPE type, String id, T exp, int line){
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
        this.line = line;
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
                  2. If we are in class- no variable with this name in parent class*/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.id) != null)
            throw new SemanticException(this);

        /* If we are not in a class check there is no variable (CFIELD) with this name in parents classes */
        if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS){
            if (SYMBOL_TABLE.getInstance().findInInheritance(this.id) != null) {
                throw new SemanticException(this);
            }
        }

        /* Check: type can be instanced (is in AST_TYPE) but not VOID */
        TYPE typeToAssign = this.type.SemantMe();
        if (typeToAssign instanceof TYPE_VOID)
        {
            throw new SemanticException(this);
        }

        /*  Compare types if there is an ASSIGNMENT
         *  4 options: 1. assign class variable
         *             2. assign array variable
         *             3. assign int
         *             4. assign string
         * BUT- if it's vardec in CFIELD(we are in a class), can only assign string, int, null*/
        if (this.exp != null)
        {
            TYPE expType = this.exp.SemantMe();
            /* vardec is CFIELD- just const string/int/null assignments*/
            if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS)
            {
                if (!(this.exp instanceof AST_EXP_OPT)) {
                    throw new SemanticException(this);
                }

                /* TYPE_NIL only on TYPE_CLASS or TYPE_ARRAY*/
                if (expType instanceof TYPE_NIL)
                {
                    if (!(typeToAssign instanceof TYPE_CLASS || typeToAssign instanceof TYPE_ARRAY)) {
                        throw new SemanticException(this);
                    }
                }
            }
            else
            {
                /* We are inside a function/ method/ global scope/ if/ while
                * check that the assigned type is matched*/
                if (!typeToAssign.checkAssign(expType)) {
                    throw new SemanticException(this);
                }
            }
        }

        TYPE_VAR currVar = new TYPE_VAR(this.id, typeToAssign);
        SYMBOL_TABLE.getInstance().enter(this.id, currVar, false);
        return currVar;
    }
}

