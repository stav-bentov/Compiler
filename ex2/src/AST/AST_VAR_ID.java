package AST;

public class AST_VAR_ID extends AST_VAR
{
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_ID(String id) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID(%s)\n",id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.id = id;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST ID VAR */
		/*********************************/
		System.out.print("AST NODE VAR ID\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		System.out.format("ID(%s)\n",id);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ID(%s)",id));
	}
}
