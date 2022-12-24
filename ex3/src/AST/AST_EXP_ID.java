package AST;

public class AST_EXP_ID extends AST_EXP
{
	public String id;
	public AST_VAR var;
	public AST_LIST<AST_EXP> l;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_ID(String id, AST_VAR var, AST_LIST<AST_EXP> l) {
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
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP ID\n");

		/*****************************/
		/* RECURSIVELY PRINT Non-Terminals ... */
		/*****************************/
		if (var != null) var.PrintMe();
		System.out.format("ID(%s)\n",id);
		if (l != null) l.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\n%s\nCOMMA_EXP_LIST\n", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (l != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);
	}

}