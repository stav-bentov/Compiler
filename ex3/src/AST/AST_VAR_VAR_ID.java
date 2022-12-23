package AST;

import src.TYPES.TYPE_VAR;

public class AST_VAR_VAR_ID extends AST_VAR
{
	public AST_VAR var;
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_VAR_ID(AST_VAR var, String id) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== var -> var DOT ID(%s)\n",id);
		this.var = var;
		this.id = id;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE ID VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("ID(%s)\n",id);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ID\nVAR\n...->%s",id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public SemantMe() throws SemanticException{
		TYPE_VAR first_var_type = this.var.SemantMe();
		if(!first_var_type.isClass()){
			throw new SemanticException(String.format("cannot access fields of %s - not a class"));
		}
		return GetDataMemberVarType(first_var_type, this.id);
	}

	public GetDataMemberVarType(TYPE_VAR first_var_type, String id) throws SemanticException{
		TYPE_LIST head = first_var_type.type.DataMembers.head;
		while(head != null){
			if (head.name == id){
				//if type_var is not of TYPE_VAR, will fail on SemantMe
				TYPE_VAR type_var = new TYPE_VAR(this.id, SYMBOL_TABLE.getInstance().find(id));
				return type_var.SemantMe();
			}

			head = head.tail;
		}

		throw new SemanticException(String.format("%s not a data member of class %s"), id, first_var_type.name);

	}
}
