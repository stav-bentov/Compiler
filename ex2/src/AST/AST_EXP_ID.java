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
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (var != null and l != null)
			System.out.print("====================== exp ->  [ var DOT ] ID(%s)([ exp [ COMMA exp ]*])", id);
		if (var != null and l = null)
			System.out.print("====================== exp ->  [ var DOT ] ID(%s)()", id);
		if (var = null and l != null)
			System.out.print("====================== exp ->  [ var DOT ] ID(%s)([ exp [ COMMA exp ]*])", id);
		if (var = null and l = null)
			System.out.print("====================== exp ->  ID(%s)()", id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
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
			"VAR\n%s\nCOMMA_EXP_LIST\n", id);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
		if (l != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
	}
}