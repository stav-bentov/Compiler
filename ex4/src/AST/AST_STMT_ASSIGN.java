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
		TYPE_VAR assignVar = (TYPE_VAR) this.var.SemantMe();
		TYPE expType = this.exp.SemantMe();

		if (!checkAssign(assignVar, expType, this.exp))
			throw new SemanticException(this);

		return null;
	}

	@Override
	public TEMP IRme()
	{
		/* ASSUMPTION: this.exp != null*/
		TEMP exp_temp = this.exp.IRme();

		/*  Case 1: var = new exp (Only for class or array) or var = exp
		 *  Case 2: var.id = new exp (Only for class or array) or var = exp
		 *  Case 3: var[exp] = new exp (Only for class or array) or var = exp
		 *  SemantMe on var will return TYPE_VAR */

		/* Case 1: (var instance of AST_VAR_ID)*/
		/* Case 2: (var instance of AST_VAR_VAR_ID) -> goes to case field as well */
		if (var instanceof AST_VAR_ID || var instanceof AST_VAR_VAR_ID)
		{
			switch (var.var_type)
			{
				case GLOBAL:
					/* Update/Set global variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Update_Global_Var(var.global_var_label, exp_temp));
					break;
				case LOCAL:
				case ARGUMENT:
					/* Update/Set argument/local variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Stack_Var(var.var_offset, exp_temp));
					break;
				case FIELD:
					/* Update/Set class field variable*/
					IR.getInstance().Add_IRcommand(new IRcommand_Assign_Field(var.var_offset, exp_temp, var.VTLabel));
					/* I think that it can't be a field but maybe I'm missing*/
					break;
			}
		}

		/* Case 3: (var instance of AST_VAR_EXP)*/
		if (var instanceof AST_VAR_EXP)
		{
			/* Need to get the array (var.var.IRme()) and index (var.exp.IRme())
			   because we need to CHANGE them (not to get their value..)*/
			AST_VAR_EXP var_exp = (AST_VAR_EXP) this.var;
			TEMP array_temp = var_exp.var.IRme();
			TEMP index_temp = var_exp.exp.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Update_Array_Var(array_temp, index_temp, exp_temp));
		}

		return null;
	}
}
