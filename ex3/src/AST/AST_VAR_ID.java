package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_ID extends AST_VAR
{
	public String id;
	
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
		TYPE var = SYMBOL_TABLE.getInstance().findWithPriority(this.id);
		if(var == null || !var.isVar())
			throw new SemanticException(this);

		return var;
	}
}
