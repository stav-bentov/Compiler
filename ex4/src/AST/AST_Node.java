package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;
import IR.IR;
import IR.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/

	public int line;

	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
	
	public abstract TYPE SemantMe() throws SemanticException;

	public abstract TEMP IRme();

	public boolean checkAssign(TYPE_VAR assignVar, TYPE expType, AST_Node exp)
	{
		/*  Case 1: var = new exp (Only for class or array) or var = exp (need to be from same type)
		 *  Case 2: var.id = new exp (Only for class or array) or var = exp (need to be from same type)
		 *  Case 3: var[exp] = new exp (Only for class or array) or var = exp (need to be from same type)
		 *  SemantMe on var will return TYPE_VAR */

		/* new _: valid only on Arrays and Classes */
		if (exp instanceof AST_NEW_EXP_NEW_TYPE_EXP)
		{
			if (assignVar.type instanceof TYPE_ARRAY)
			{
				/* A = new type[exp] -> check A.arrayType = type*/
				if (((AST_NEW_EXP_NEW_TYPE_EXP) exp).exp != null)
				{
					TYPE arrayType = ((TYPE_ARRAY) assignVar.type).arrayType;
					// Allow inheritance
					if (arrayType instanceof TYPE_CLASS)
					{
						if (expType instanceof TYPE_CLASS && ((TYPE_CLASS) expType).inheritsFrom((TYPE_CLASS) arrayType))
						{
							return true;
						}
					}
					else if (arrayType == expType)
					{
						return true;
					}
				}
				// else- A = new type (Not allows because A is an array)
			}

			else if (assignVar.type instanceof TYPE_CLASS)
			{
				/* A = new B -> check A.type = B OR B inherits from A.type*/
				if (((AST_NEW_EXP_NEW_TYPE_EXP) exp).exp == null)
				{
					if (expType instanceof TYPE_CLASS && ((TYPE_CLASS) expType).inheritsFrom((TYPE_CLASS) assignVar.type))
						return true;
				}
			}
			// In every other assignments (string or int)- can't be a new
		}
		else
		{// exp instanceof AST_EXP (no new)
			// Only for class or array
			if (expType instanceof TYPE_NIL)
			{
				if (assignVar.type instanceof TYPE_ARRAY || assignVar.type instanceof TYPE_CLASS)
					return true;
			}
			else
			{ // expType != null
				/* In this case: assignVar = something with type expType*/
				if (assignVar.type instanceof TYPE_CLASS)
				{/* className a = b -> b is from TYPE_CLASS and equal or inheritance from the className*/
					if (expType instanceof TYPE_CLASS && ((TYPE_CLASS) expType).inheritsFrom((TYPE_CLASS) assignVar.type))
						return true;
				}
				/* arrayName a = b -> b is from type array and equal to arrayName*/
				else if (assignVar.type instanceof TYPE_ARRAY)
				{
					if (expType instanceof TYPE_ARRAY)
					{
						if (assignVar.type == expType)
							return true;
					}
				}
				/* string/int a*/
				else
				{
					if (assignVar.type == expType)
						return true;
				}
			}
		}
		return false;
	}

	public TYPE CheckCallToFunc(String id, AST_VAR var, AST_LIST<AST_EXP> l) throws SemanticException {
		TYPE typeFound;

		/* Find type */
		if (var == null) {
			typeFound = SYMBOL_TABLE.getInstance().find(id);
		}
		else {
			TYPE typeOfVar = ((TYPE_VAR)var.SemantMe()).type;
			if (!typeOfVar.isClass()) {
				throw new SemanticException(this);
			}
			TYPE_CLASS typeClassOfVar = (TYPE_CLASS)typeOfVar;
			typeFound = SYMBOL_TABLE.getInstance().findInInheritance(id, typeClassOfVar);
		}

		/* Check if type was found */
		if (typeFound == null) {
			throw new SemanticException(this);
		}

		/* Check if it's a method */
		if (!(typeFound instanceof TYPE_FUNCTION)) {
			// There is no way a function with this name exists,
			// because this type wouldn't have been inserted to this symbol table in the first place
			throw new SemanticException(this);
		}

		/* Check call */
		CallToFuncMatchesFunc((TYPE_FUNCTION)typeFound, l); // We've already made sure typeFound is of TYPE_FUNC

		return ((TYPE_FUNCTION) typeFound).returnType;
	}

	private void CallToFuncMatchesFunc(TYPE_FUNCTION func, AST_LIST<AST_EXP> l) throws SemanticException{
		//if our func has no params and the called func has params - throw error
		if((l == null && func.params != null) || (func.params == null && l != null)){
			throw new SemanticException(this);
		}

		//if they are both null all is good darling
		if(l != null && func.params != null){
			/* Check if parameters match expected parameters */
			TYPE_LIST params = (TYPE_LIST) l.SemantMe(); // l.SemantMe() supposed to return TYPE_LIST
			if (!func.params.equalsForValidatingGivenParams(params)) {
				throw new SemanticException(this);
			}
		}
	}

	/* In case of AST that represent a variablel*/
	enum VarType{
		GLOBAL,
		ARGUMENT,
		LOCAL,
		FIELD
	}

	/* There are 3 types of var:
        1. Global variable (In this case we will need to access it from Data area with the correct label)
        2. Argument variable (In this case we will need to access it from Stack with the correct offset above $fp)
        3. Local variable (In this case we will need to access it from Stack with the correct offset down $fp)
        each one of them - can be:
        1. A "simple" variable - from type String/ Int
        2. A class variable (with accessing c-fields)
        3. An array variable
     */
	public String global_var_label;
	public int var_offset;
	public VarType var_type;
	public String VTLabel;

	public void set_global(String global_var_label)
	{
		this.global_var_label = global_var_label;
		this.var_type = VarType.GLOBAL;
	}

	public void set_argument(int num_var)
	{
		/* 8 because we need to pass return address*/
		this.var_offset = 8 + (num_var * 4);
		this.var_type = VarType.ARGUMENT;
	}

	public void set_local(int num_var)
	{
		/* -44 because we need to pass prev fp and register backup*/
		this.var_offset = -44 - (num_var * 4);
		this.var_type = VarType.LOCAL;
	}

	public void set_field(int num_fields, String VTLabel)
	{
		this.var_offset = 4 + (num_fields * 4); // Offset in the runtime object
		this.var_type = VarType.FIELD;
		this.VTLabel = VTLabel;
	}

	public TEMP_LIST build_param_list(AST_LIST ast_param_list)
	{
		AST_LIST pointer = ast_param_list;
		TEMP param_temp;
		TEMP_LIST temp_list = null;
		int temp_list_len = 0;

		while (pointer != null)
		{
			param_temp = pointer.head.IRme();
			temp_list = new TEMP_LIST(param_temp, null);
			pointer = pointer.tail;
			temp_list_len++;

			while(pointer != null)
			{
				param_temp = pointer.head.IRme();
				temp_list.tail = new TEMP_LIST(param_temp, temp_list.tail);
				temp_list_len++;
				pointer = pointer.tail;
			}
		}

		temp_list.len = temp_list_len;
		return temp_list;
	}

}
