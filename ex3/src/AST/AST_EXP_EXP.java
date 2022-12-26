package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_EXP extends AST_EXP
{
	public AST_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_EXP(AST_EXP exp, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== exp -> (exp)\n");
		this.exp = exp;
		this.line = line;
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (exp != null) exp.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"EXP\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		return this.exp.SemantMe();
	}
}
