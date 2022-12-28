package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_ID extends AST_EXP
{
	public String id;
	public AST_VAR var;
	public AST_LIST<AST_EXP> l;

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
		this.l = l;
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
		if (l != null)
			l.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("exp ID: %s\n", this.id));
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (l != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);
	}

	//calling a function
	public TYPE SemantMe() throws SemanticException {
		return CheckCallToFunc(id, var, l);
	}
}