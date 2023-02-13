package AST;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR(AST_VAR var, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== exp -> var\n");
		this.var = var;
		this.line = line;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		System.out.print("exp\n");

		if (var != null)
			var.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("exp var \n"));
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException{
		//this class is of type expression and therefore should return TYPE of the var (and not TYPE_VAR)
		return ((TYPE_VAR) this.var.SemantMe()).type;
	}

	@Override
	public TEMP IRme()
	{
		return this.var.IRme();
	}
}
