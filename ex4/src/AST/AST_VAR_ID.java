package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_ID extends AST_VAR
{
	public String id;
	public TYPE_VAR typeVar;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_ID(String id, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== var -> ID(%s)\n",id);
		this.id = id;
		this.line = line;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		System.out.print("var id\n");

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("var id: %s",id));
	}

	public TYPE SemantMe() throws SemanticException{
		//simple case of var: id. get the var from symbol table according to the id, and then check that it is indeed a var
		TYPE var = SYMBOL_TABLE.getInstance().find(this.id);
		if(var == null || !var.isVar())
			throw new SemanticException(this);

		/* Set values to type_var (and label/ offset), according to var from SYMBOL_TABLE*/
		/* ASSUMPTION!!: var is TYPE_VAR!*/
		typeVar = (TYPE_VAR) var;

		return var;
	}

	@Override
	public TEMP IRme()
	{
		TEMP var_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		switch(this.typeVar.var_type) {
			case GLOBAL:
				IR.getInstance().Add_IRcommand(new IRcommand_Get_Global_Var(this.typeVar.global_var_label, var_temp));
				break;
			case LOCAL:
				IR.getInstance().Add_IRcommand(new IRcommand_Get_Local_Var(this.typeVar.var_offset, var_temp));
				break;
			case ARGUMENT:
				IR.getInstance().Add_IRcommand(new IRcommand_Get_Argument_Var(this.typeVar.var_offset, var_temp));
				break;
			case FIELD:
				// var = this.field
				IR.getInstance().Add_IRcommand(new IRcommand_Get_Class_Var(this.typeVar.var_offset, var_temp));
				break;
		}
		return var_temp;
	}
}
