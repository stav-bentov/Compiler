package AST;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

public class AST_EXP_ID extends AST_EXP
{
	public String id;
	public AST_VAR var;
	public AST_LIST<AST_EXP> parmeters_list;
	public TYPE_FUNCTION func;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_ID(String id, AST_VAR var, AST_LIST<AST_EXP> l, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if (var != null && l != null)
			System.out.printf("====================== exp -> var DOT ID(%s)([commaExpList])", id);
		else if (var != null)
			System.out.printf("====================== exp -> var DOT ID(%s)()", id);
		else if (l != null)
			System.out.printf("====================== exp -> var DOT ID(%s)([commaExpList])", id);
		else
			System.out.printf("====================== exp -> ID(%s)()", id);
		this.id = id;
		this.var = var;
		this.parmeters_list = l;
		this.line = line;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		System.out.format("exp\n");

		if (var != null)
			var.PrintMe();
		System.out.format("exp ID: %s\n", this.id);
		if (parmeters_list != null)
			parmeters_list.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("exp ID: %s\n", this.id));
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (parmeters_list != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, parmeters_list.SerialNumber);
	}

	//calling a function
	public TYPE SemantMe() throws SemanticException {
		func = CheckCallToFunc(id, var, parmeters_list);

		return func.returnType;
	}

	@Override
	public TEMP IRme()
	{
		TEMP result_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP_LIST temp_list = this.parmeters_list == null ? null : build_param_list(this.parmeters_list);
		if (this.var == null)
		{
			if (func.isMethod) {
				IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, result_temp, func.offset));
			}
			else { // Is global func
				IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func(temp_list, result_temp, func.func_label));
			}
			/* TODO: I don't think that function from type void will be called- ASK SOMEONE...*/
		}
		else // Has to be a method
		{
			/* this.var is a class instance, were trying to access its VT
		       this.var.IRme will return the class pointer, which hase VT ptr in offset 0 */
			TEMP classPtr = this.var.IRme();

			IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, result_temp, func.offset, classPtr));
		}
		return result_temp;
	}
}