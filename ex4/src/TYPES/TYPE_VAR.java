package TYPES;

import AST.AST_Node;
import IR.IRcommand;
import TEMP.TEMP;
import jdk.nashorn.internal.ir.Symbol;

public class TYPE_VAR extends TYPE{
    public enum VarType{
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
    public int var_offset;
    public VarType var_type;
    public TYPE type;
    public int initial_cfield_int_value = -1;
    public String initial_cfield_str_value = null;

    public TYPE_VAR(String name, TYPE type){
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isVar(){ return true; }

    public void set_global(String global_var_label)
    {
        this.global_var_label = IRcommand.getFreshLabel(global_var_label);
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

    public void set_field(int num_fields, int initial_cfield_int_value)
    {
        set_field(num_fields);
        this.initial_cfield_int_value = initial_cfield_int_value;
    }

    public void set_field(int num_fields, String initial_cfield_str_value)
    {
        set_field(num_fields);
        this.initial_cfield_str_value = initial_cfield_str_value;
    }

    public void set_field(int num_fields)
    {
        this.var_offset = 4 + (num_fields * 4); // Offset in the runtime object
        this.var_type = VarType.FIELD;
    }
}
