package AST;

import src.TYPES.TYPE_VAR;

public class AST_VAR_ID extends AST_VAR
{
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_ID(String id) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== var -> ID(%s)\n",id);
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

	public TYPE SemantMe() throws SemanticException{
		TYPE_VAR type_var = new TYPE_VAR(this.id, SYMBOL_TABLE.getInstance().find(id));
		if(type_var.type == null || !type_var.type.isVar())
			throw new SemanticException(String.format("%s referenced before declaration or is not a var!", this.id));

		return type_var;
	}
}
