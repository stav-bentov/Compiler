package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_EXP extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_EXP(AST_VAR var, AST_EXP exp, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if (exp == null)
			System.out.format("====================== var -> var\n");
		else
			System.out.format("====================== var -> var [exp]\n");
		this.var = var;
		this.exp = exp;
		this.line = line;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		System.out.print("var exp\n");

		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber, "var exp\n");
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException{
		//If var is not of TYPE_VAR SemantMe will throw an error
		TYPE_VAR type_var = (TYPE_VAR)var.SemantMe();

		//checking if type_var is an array
		if(!type_var.type.isArray()){
			throw new SemanticException(this);
		}
		TYPE arrayType = ((TYPE_ARRAY)type_var.type).arrayType;

		//checking if there was a legal access to the array
		TYPE type_exp = this.exp.SemantMe();
		if(!(type_exp instanceof TYPE_INT)){
			throw new SemanticException(this);
		}

		//type_var.type is TYPE_ARRAY - which should have a field called type - which is why type of array it is.
		//after speaking to lilach we realized the name does not matter. returning name null.
		return new TYPE_VAR(null, arrayType);
	}

	@Override
	public TEMP IRme()
	{
		TEMP array_temp = this.var.IRme();
		TEMP index_temp = this.exp.IRme();
		TEMP array_access_temp = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IRcommand_Array_Index_Access(array_temp, index_temp, array_access_temp));

		return array_access_temp;
	}
}
