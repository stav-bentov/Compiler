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

	//calling a function
	public TYPE SemantMe() throws SemanticException {
		TYPE typeFound;

		//find type - assuming this is a method of class.
		//either method of the current class scope, or a method of already defined class.
		if (var == null) {
			typeFound = SYMBOL_TABLE.getInstance().find(this.id);
		} else {
			//if var != null then this is a method of a class. we get the class, and then find the method in its inheritance tree
			TYPE typeOfVar = var.SemantMe().type;
			//if typeOfVar is not a class, then this.id does not represent a method, and therefore error
			if (!typeOfVar.isClass()) {
				throw new SemanticException("This type is not a class and therefore does not have class methods", this);
			}

			TYPE_CLASS typeClassOfVar = (TYPE_CLASS)typeOfVar;
			typeFound = SYMBOL_TABLE.getInstance().findInInheritance(this.id, typeClassOfVar);
		}

		if (typeFound == null) {
			/* If not found the function can be a global function, check in global scope */
			if (var == null) {
				typeFound = findInGlobalScope(this.id);
			}

			/* Check if it is still not found */
			if (typeFound == null) {
				/* Now it really does not exist */
				throw new SemanticException("Function does not exist", this);
			}

			if (!(typeFound instanceof TYPE_FUNCTION)) {
				// There is no way a function with this name exists,
				// because this type wouldn't have been inserted to this symbol table in the first place
				throw new SemanticException("This function does not exist in an open scope", this);
			}

			CallToFuncMatchesFunc((TYPE_FUNCTION)typeFound); // We've already made sure typeFound is of TYPE_FUNC
			return (TYPE_FUNCTION) typeFound.returnType;
		}
	}

	private void CallToFuncMatchesFunc(TYPE_FUNCTION func) {
		/* Check if parameters match expected parameters */
		TYPE_LIST params = (TYPE_LIST) l.SemantMe(); // l.SemantMe() supposed to return TYPE_LIST
		if (!func.params.validateGivenParam(params)) {
			throw new SemanticException("Parameters received do not match expected parameters", this);
		}
	}
}