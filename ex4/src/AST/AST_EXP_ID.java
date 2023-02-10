package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_ID extends AST_EXP
{
	public String id;
	public AST_VAR var;
	public AST_LIST<AST_EXP> parmeters_list;
	public String func_prologe_label;
	public String class_name = "";

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
		/* TODO: for Lilach take care of class_name*/
		if (this.var != null)
		{
			/*....*/
		}

		/* Set func_label*/
		if (!this.id.equals("main"))
		{
			this.func_prologe_label = "start_" + this.id + "_" + class_name;
		}
		else
		{
			this.func_prologe_label = this.id;
		}

		return CheckCallToFunc(id, var, parmeters_list);
	}

	@Override
	public TEMP IRme()
	{
		TEMP result_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP_LIST temp_list = build_param_list(this.parmeters_list);
		if (this.var == null)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func_With_Return(this.func_prologe_label, temp_list, result_temp));
			/* TODO: I don't think that function from type void will be called- ASK LILACH/ROTEM...*/
            /* Functions: 1. PrintString
                          2. PrintInt
                          (For 1 and 2 there is only one parameter = param_temp.head)
                          3. global functions
             */
			/*if (this.id.equals("PrintString"))
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_String(temp_list.head));
			}
			else if (this.id.equals("PrintInt"))
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_Int(temp_list.head));
			}
			else
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func_With_Return(this.func_prologe_label, temp_list, result_temp));
			}*/
		}
		else
		{/* TODO: For Lilach take care of methods */

		}
		return null;
	}
}