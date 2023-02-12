package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_VAR_ID extends AST_VAR
{
	public AST_VAR var;
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_VAR_ID(AST_VAR var, String id, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== var -> var DOT ID(%s)\n",id);
		this.var = var;
		this.id = id;
		this.line = line;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		System.out.print("var id\n");

		if (var != null)
			var.PrintMe();
		System.out.format("var id: %s\n",id);

		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("var id:",id));
	}

	public TYPE SemantMe() throws SemanticException{
		// first var should represent a var of a class. second should be a datamember of that class.
		// SemantMe checks that this var exists, and it is indeed of TYPE_VAR
		TYPE_VAR first_var_type = (TYPE_VAR)var.SemantMe();
		//first_var_type.type is the desired class
		if(!first_var_type.type.isClass()){
			throw new SemanticException(this);
		}

		//find the field in the inheritance tree of the class
		TYPE type = SYMBOL_TABLE.getInstance().findInInheritance(this.id, (TYPE_CLASS)first_var_type.type);
		//if not found throw error
		if(type == null){
			throw new SemanticException(this);
		}

		//type has to be a var or class (type is class in case of something like: house.room.size - so house.room is a class)
		//type is a method is handled in AST_EXP_ID
		if(!type.isVar()){
			throw new SemanticException(this);
		}

		first_var_type.set_AST_from_TYPE_VAR(this);

		//return the TYPE_VAR type
		return type;
	}

	@Override
	public TEMP IRme() {
		TEMP var_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		/* this.var is a class instance, were trying to assign a value to a field of that instance
		   this.var.IRme will return the class pointer */
		TEMP classPtr = this.var.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_Get_Class_Var(this.var_offset, classPtr, var_temp));

		return var_temp;
	}

//previous implementation. leaving it here in case it is neccesary for future implementation.
//	public TYPE_VAR GetDataMemberVarType(TYPE_CLASS type_class, String id) throws SemanticException{
//		//iterate over all datamembers of type_class, check if id exists. if it does, get it from inheritance tree
//		TYPE_LIST head = type_class.data_members.head;
//		while(head != null){
//			if (head.name == id){
//				//if type_var is not of TYPE_VAR, will fail on SemantMe
//				TYPE_VAR type_var SYMBOL_TABLE.getInstance().find(id);
//				return type_var.SemantMe();
//			}
//
//			head = head.tail;
//		}
//
//		throw new SemanticException(this);
//
//	}
}
