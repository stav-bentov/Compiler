package AST;
import TEMP.TEMP;
import TYPES.*;
import IR.*;

public class AST_STMT_ASSIGN<T extends AST_Node> extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public T exp;

	public TYPE_VAR typeVar;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,T exp, int line)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		String expType = "";
		if (exp instanceof AST_EXP) {
			expType = "exp";
		} else if (exp instanceof AST_NEW_EXP) {
			expType = "newExp";
		}
		String deriveRule = String.format("====================== stmt -> var ASSIGN %s SEMICOLON\n", expType);
		System.out.print(deriveRule);
		this.var = var;
		this.exp = exp;
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("stmt assign\n");

		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"stmt assign\n");

		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		typeVar = (TYPE_VAR) this.var.SemantMe();
		TYPE expType = this.exp.SemantMe();

		if (!checkAssign(typeVar, expType, this.exp))
			throw new SemanticException(this);

		return null;
	}

	@Override
	public TEMP IRme()
	{
		/* ASSUMPTION: this.exp != null*/
		TEMP exp_temp = null;

		/* Set var values*/
		TEMP classPtr, array_temp, index_temp;
		classPtr = array_temp = index_temp = null;

		if (this.var instanceof AST_VAR_VAR_ID) {
			classPtr = ((AST_VAR_VAR_ID) this.var).var.IRme(); // classPtr is the ptr to the runtime obj of classInstance
		}

		if (this.var instanceof AST_VAR_EXP) {
			/* Need to get the array (var.var.IRme()) and index (var.exp.IRme())
			   because we need to CHANGE them (not to get their value..)*/
			System.out.println("IN HERE...");
			AST_VAR_EXP var_exp = (AST_VAR_EXP) this.var;
			array_temp = var_exp.var.IRme();
			index_temp = var_exp.exp.IRme();
		}

		if (this.exp instanceof AST_EXP_OPT)
		{
			/* Constant*/
			AST_EXP_OPT exp_opt = (AST_EXP_OPT)this.exp;
			switch (exp_opt.opt) {
				case "MINUS INT":
				case "INT":
					int int_value = ((AST_EXP_OPT) this.exp).i;
					if (exp_opt.opt.equals("MINUS INT"))
					{
						int_value = -int_value;
					}
					/* Add IR Command*/
					run_IRme(null, int_value, null, true, false, classPtr, array_temp, index_temp);
					break;
				case "STRING":
					String str_value = ((AST_EXP_OPT) this.exp).s;
					/* Add IR Command*/
					run_IRme(str_value, 0, null, false, true, classPtr, array_temp, index_temp);
					break;
				case "NIL": /*(pass zero as null)*/
					run_IRme(null, 0, null, true, false, classPtr, array_temp, index_temp);
			}
		}
		else
		{
			/* Not a constant -> run IRme()*/
			exp_temp = this.exp.IRme();
			run_IRme(null, 0, exp_temp, false, false, classPtr, array_temp, index_temp);

		}
		return null;
	}

	public void run_IRme(String str, int i, TEMP temp, Boolean is_int, Boolean is_str, TEMP classPtr, TEMP array_temp, TEMP index_temp)
	{

		/*  Case 1: var = new exp (Only for class or array) or var = exp
		 *  Case 2: var.id = new exp (Only for class or array) or var = exp
		 *  Case 3: var[exp] = new exp (Only for class or array) or var = exp
		 *  SemantMe on var will return TYPE_VAR */
		if (is_str)
		{
			/* Case 1: (var instance of AST_VAR_ID)*/
			if (this.var instanceof AST_VAR_ID) {
				if (typeVar.var_type == TYPE_VAR.VarType.GLOBAL) {
					/* Update/Set global variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Update_Global_Var(typeVar.global_var_label, str));
				}
				else if (typeVar.var_type == TYPE_VAR.VarType.FIELD){
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, str));
				}
				else
				{
					/* Update/Set argument/local variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(typeVar.var_offset, str));
				}
			}
			/* Case 2: (var instance of AST_VAR_VAR_ID) */
			if (this.var instanceof AST_VAR_VAR_ID)
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, classPtr, str));
			}
			/* Case 3: (var instance of AST_VAR_EXP)*/
			if (this.var instanceof AST_VAR_EXP)
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Update_Array_Var(array_temp, index_temp, str));
			}
		}
		else if (is_int)
		{
			/* Case 1: (var instance of AST_VAR_ID)*/
			if (this.var instanceof AST_VAR_ID) {
				if (typeVar.var_type == TYPE_VAR.VarType.GLOBAL) {
					/* Update/Set global variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Update_Global_Var(typeVar.global_var_label, i));
				}
				else if (typeVar.var_type == TYPE_VAR.VarType.FIELD){
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, i));
				}
				else {
					/* Update/Set argument/local variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(typeVar.var_offset, i));
				}
			}
			/* Case 2: (var instance of AST_VAR_VAR_ID) */
			if (this.var instanceof AST_VAR_VAR_ID)
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, classPtr, i));
			}
			/* Case 3: (var instance of AST_VAR_EXP)*/
			if (this.var instanceof AST_VAR_EXP)
			{
				System.out.println("HERE!!!!!");
				IR.getInstance().Add_IRcommand(new IRcommand_Update_Array_Var(array_temp, index_temp, i));
			}
		}
		else
		{
			/* Case 1: (var instance of AST_VAR_ID)*/
			if (this.var instanceof AST_VAR_ID) {
				if (typeVar.var_type == TYPE_VAR.VarType.GLOBAL) {
					/* Update/Set global variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Update_Global_Var(typeVar.global_var_label, temp));
				}
				else if (typeVar.var_type == TYPE_VAR.VarType.FIELD){
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, temp));
				}
				else {
					/* Update/Set argument/local variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(typeVar.var_offset, temp));
				}
			}
			/* Case 2: (var instance of AST_VAR_VAR_ID) */
			if (this.var instanceof AST_VAR_VAR_ID)
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(typeVar.var_offset, temp, classPtr));
			}
			/* Case 3: (var instance of AST_VAR_EXP)*/
			if (this.var instanceof AST_VAR_EXP)
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Update_Array_Var(array_temp, index_temp, temp));
			}
		}
	}
}
