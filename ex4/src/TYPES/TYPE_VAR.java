package TYPES;

import AST.AST_Node;
import jdk.nashorn.internal.ir.Symbol;

public class TYPE_VAR extends TYPE{
    enum VarType{
        GLOBAL,
        ARGUMENT,
        LOCAL,
        FIELD
    }

    /* There are 4 types of var:
        1. Global variable (In this case we will need to access it from Data area with the correct label)
        2. Argument variable (In this case we will need to access it from Stack with the correct offset above $fp)
        3. Local variable (In this case we will need to access it from Stack with the correct offset down $fp)
        4. Field (of a class)
        each one of them - can be:
        1. A "simple" variable - from type String/ Int
        2. A class variable (with accessing c-fields)
        3. An array variable
     */
    public String global_var_label;
    public AST_Node exp; // For fields. Can be AST_NEW_EXP or AST_EXP
    public int var_offset;
    public VarType var_type;

    public TYPE type;

    public TYPE_VAR(String name, TYPE type){
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isVar(){ return true; }

    public void set_global(String global_var_label)
    {
        this.global_var_label = global_var_label;
        this.var_type = VarType.GLOBAL;
    }

    public void set_argument(int num_var)
    {
        this.var_offset = 8 + (num_var * 4);
        this.var_type = VarType.ARGUMENT;
    }

    public void set_local(int num_var)
    {
        this.var_offset = -44 - (num_var * 4);
        this.var_type = VarType.LOCAL;
    }

    public void set_field(int num_fields, AST_Node exp)
    {
        this.var_offset = 4 + (num_fields * 4); // Offset in the runtime object
        this.var_type = VarType.FIELD;
        this.exp = exp;
    }

    /* TODO: For Lilach- take care of fields variables*/

    public void set_AST_from_TYPE_VAR(AST_Node curr_ast)
    {
        switch(this.var_type) {
            case GLOBAL:
                curr_ast.set_global(this.global_var_label);
                break;
            case ARGUMENT:
                curr_ast.set_argument(this.var_offset);
                break;
            case LOCAL:
                curr_ast.set_local(this.var_offset);
                break;
            case FIELD:
                curr_ast.set_field(this.var_offset);
                break;
        }
    }
}
