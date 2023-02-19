package TYPES;

import IR.IRcommand;
import MIPS.MIPSGenerator;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;

	public int num_local_variables;

	public boolean isMethod;
	public int offset;
	public String func_label;
	public String epilogue_func_label;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.num_local_variables = 0;
		this.func_label = IRcommand.getFreshLabel("start_" + name);
		this.epilogue_func_label = IRcommand.getFreshLabel("epilogue_" + this.name);
		if (name.equals("main"))
		{
			this.func_label = MIPSGenerator.user_main;
			this.epilogue_func_label = "epilogue_" + MIPSGenerator.user_main;
		}
	}

	@Override
    /* Receives Object o
       If it's not from TYPE_FUNCTION- returns false
       else- Checks if it has the same signature (same returned type, arguments types and names).
       Returns true- same signature, else- false*/
	public boolean equals(Object o)
	{
		if (!(o instanceof TYPE_FUNCTION)) return false;

		TYPE_FUNCTION compered_func = (TYPE_FUNCTION) o;
		/* Compare: 1. names,
					2. return types
					3. parameter's types */
		if (!(this.name.equals(compered_func.name))) return false;

		/* TODO: check if inherited return type is an override */
		if (this.returnType != compered_func.returnType) return false;

		//if one function has no params and other have params then they are not equal
		if((this.params == null && compered_func.params != null) || (compered_func.params == null && this.params != null)){
			return false;
		}

		//perform this check only if both are not null
		if(this.params != null && compered_func.params != null){
			if (!(this.params.equalsForComparingSignature(compered_func.params))) return false;
		}

		return true;
	}
}
