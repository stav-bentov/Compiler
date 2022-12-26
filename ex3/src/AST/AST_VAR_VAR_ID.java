package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

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
		// first var should represent a var of a class. second should be a datamember of that class.
		// SemantMe checks that this var exists, and it is indeed of TYPE_VAR
		TYPE_VAR first_var_type = this.var.SemantMe();
		//first_var_type.type is the desired class
		if(!first_var_type.type.isClass()){
			throw new SemanticException(String.format("cannot access fields of %s - not a class"));
		}

		//find the field in the inheritance tree of the class
		TYPE type = SYMBOL_TABLE.getInstance().findInInheritance(this.id, first_var_type.type);
		//if not found throw error
		if(type == null){
			throw new SemanticException(String.format("%s is not a field of %s or any of its ancsetors", this.id, first_var_type.type.name));
		}

		//type has to be a var or class (type is class in case of something like: house.room.size - so house.room is a class)
		//type is a method is handled in AST_EXP_ID
		if(!type.isVar() && !type.isClass()){
			throw new SemanticException(String.format("property %s pf class %s is not a var or a class!", this.id, first_var_type.type.name));
		}

		//return the TYPE_VAR type
		return type;
	}

	//previous implementation. leaving it here in case it is neccesary for future implementation.
	public TYPE_VAR GetDataMemberVarType(TYPE_CLASS type_class, String id) throws SemanticException{
		//iterate over all datamembers of type_class, check if id exists. if it does, get it from inheritance tree
		TYPE_LIST head = type_class.DataMembers.head;
		while(head != null){
			if (head.name == id){
				//if type_var is not of TYPE_VAR, will fail on SemantMe
				TYPE_VAR type_var SYMBOL_TABLE.getInstance().find(id);
				return type_var.SemantMe();
			}

			head = head.tail;
		}

		throw new SemanticException(String.format("%s not a data member of class %s"), id, first_var_type.name);

	}
}
