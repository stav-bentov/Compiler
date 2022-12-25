package AST;

public class AST_EXP_EXP extends AST_EXP
{
	public AST_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_EXP(AST_EXP exp) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== exp -> (exp)\n");
		this.exp = exp;
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

	public TYPE SemantMe(){
		return this.exp.SemantMe();
	}
}
