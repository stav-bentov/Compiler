package AST;
import IR.*;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC<T extends AST_Node> extends AST_Node{
    public AST_TYPE type;
    public String id;
    public T exp;
    public TYPE expType = null;
    public TYPE_VAR typeVar;

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
        System.out.format("varDec\n");

        if(type != null)
            type.PrintMe();
        if(exp != null)
            exp.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
                String.format("varDec: %s", this.id));
        if(type != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if(exp != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /* Check: 1. No other variable with this name in current scope
                  2. If we are in class- need to make sure that there is only overriding*/
        if (SYMBOL_TABLE.getInstance().findInLastScope(this.id) != null)
            throw new SemanticException(this);

        /* Check: type can be instanced (is in AST_TYPE) but not VOID */
        TYPE typeToAssign = this.type.SemantMe();
        if (typeToAssign instanceof TYPE_VOID)
        {
            throw new SemanticException(this);
        }


        /* Check that if there is a CFIELD with this name- it has the same type */
        if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS){
            TYPE searchInInheritance = SYMBOL_TABLE.getInstance().findInInheritance(this.id);
            if (searchInInheritance != null) {
                /* Error: 1. There is a CFIELD with this name and it's not TYPE_VAR- it's TYPE_FUNCTION
                          2. There is a CFIELD with this name and it's TYPE_VAR- check it's the same type
                 */
                if ((!(searchInInheritance.isVar() && ((TYPE_VAR) searchInInheritance).type == typeToAssign)) || (!searchInInheritance.isVar()))
                    throw new SemanticException(this);
            }
        }

        /*  Compare types if there is an ASSIGNMENT
         *  4 options: 1. assign class variable
         *             2. assign array variable
         *             3. assign int
         *             4. assign string
         * BUT- if it's vardec in CFIELD (we are in a class), can only assign string, int, null*/
        if (this.exp != null)
        {
            this.expType = this.exp.SemantMe();
            /* vardec is CFIELD- just const string/int/null assignments*/
            if (SYMBOL_TABLE.getInstance().getCurrentScopeType() == ScopeTypeEnum.CLASS)
            {
                if (!(this.exp instanceof AST_EXP_OPT)) {
                    throw new SemanticException(this);
                }

                /* TYPE_NIL only on TYPE_CLASS or TYPE_ARRAY*/
                if (this.expType instanceof TYPE_NIL)
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
                if (!checkAssign(new TYPE_VAR(this.id, typeToAssign), this.expType, this.exp)) {
                    throw new SemanticException(this);
                }
            }
        }

        /* 3 options: 1. variable is a local variable (in a function)
                      2. variable is a global variable (outside a function or a class)
                      3. variable is a class field (in a class, outside a function)
         */
        typeVar = new TYPE_VAR(this.id, typeToAssign);

        /* If in a function, it's a local variable- Set right offsets */
        TYPE_FUNCTION current_func = ((TYPE_FUNCTION)SYMBOL_TABLE.getInstance().getLastFunc());
        if (current_func != null)
        {
            /* Local variable */
            typeVar.set_local(current_func.num_local_variables);
            current_func.num_local_variables ++;
        }
        else
        {
            TYPE_CLASS current_class = ((TYPE_CLASS)SYMBOL_TABLE.getInstance().getCurrentClass());
            /* Global variable */
            if (current_class == null)
            {
                typeVar.set_global(this.id);
            }
            /* Class fields variable */
            else
            {
                typeVar.set_field(current_class.numFields, exp);
                current_class.numFields++;
            }
        }

        SYMBOL_TABLE.getInstance().enter(this.id, typeVar, false);
        return typeVar;
    }

    @Override
    public TEMP IRme()
    {
        TEMP assigned_temp = null;

        switch(this.typeVar.var_type) {
            case GLOBAL:
                /* No assignment declaration */
                if (this.exp == null)
                {
                    IR.getInstance().Add_IRcommand(new IRcommand_Global_Var_Dec(this.typeVar.global_var_label));
                }
                else
                {/* "You may assume that if a global variable is initialized, then the initial value is a constant (i.e., string, integer, nil)."*/
                    if (this.expType instanceof TYPE_INT)
                    {
                        /* If exp is not null and expType is TYPE_INT-> exp is instanceof AST_EXP_OPT*/
                        int int_value = ((AST_EXP_OPT) this.exp).i;
                        IR.getInstance().Add_IRcommand(new IRcommand_Global_Var_Dec(this.typeVar.global_var_label, int_value));
                    }
                    else if (this.expType instanceof TYPE_STRING)
                    {
                        /* If exp is not null and expType is TYPE_STRING-> exp is instanceof AST_EXP_OPT*/
                        String str_value = ((AST_EXP_OPT) this.exp).s;
                        IR.getInstance().Add_IRcommand(new IRcommand_Global_Var_Dec(this.typeVar.global_var_label, str_value));
                    }
                    else
                    {
                        /* it's TYPE_NIL */
                        IR.getInstance().Add_IRcommand(new IRcommand_Global_Var_Dec(this.typeVar.global_var_label, 0));
                    }
                }
                break;
            case LOCAL:
                if (this.exp != null)
                {/* there is an assignment */
                    /* Constant*/
                    if(this.exp instanceof AST_EXP_OPT)
                    {
                        if (this.expType instanceof TYPE_INT)
                        {
                            /* If exp is not null and expType is TYPE_INT-> exp is instanceof AST_EXP_OPT*/
                            int int_value = ((AST_EXP_OPT) this.exp).i;
                            if (((AST_EXP_OPT)this.exp).opt.equals("MINUS INT"))
                            {
                                int_value = -int_value;
                            }
                            IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(this.typeVar.var_offset, int_value));
                        }
                        else if (this.expType instanceof TYPE_STRING)
                        {
                            /* If exp is not null and expType is TYPE_STRING-> exp is instanceof AST_EXP_OPT*/
                            String str_value = ((AST_EXP_OPT) this.exp).s;
                            IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(this.typeVar.var_offset, str_value));
                        }
                        else
                        {
                            /* it's TYPE_NIL */
                            IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(this.typeVar.var_offset, 0));
                        }
                    }
                    else
                    {
                        /* Not a Constant -> Run IRme()*/
                        assigned_temp = this.exp.IRme();
                        IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(this.typeVar.var_offset, assigned_temp));
                    }
                }
                else
                {
                    /* exp = null then assign 0 to it
                    * TODO: check if need to initial vaules in this case*/
                    IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(this.typeVar.var_offset, 0));
                }
                break;
        }
        return null;
    }
}

