package AST;

public class AST_VAR_EXP extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_EXP(AST_VAR var, AST_EXP exp) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if (exp == null)
			System.out.format("====================== var -> var\n");
		else
			System.out.format("====================== var -> var [exp]\n");
		this.var = var;
		this.exp = exp;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE VAR EXP\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber, "VAR\nEXP\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException{
		TYPE_VAR type_var = this.var.SemantMe();
		if(!type_var.type.isArray()){
			throw new SemanticException(String.format("%s is not an array!", this.type_var.name))
		}

		TYPE type_exp = this.exp.SemantMe();
		if(!(type_exp instanceof TYPE_INT)){
			throw new SemanticException(String.format("%s is not an int - cannot put int inside brackets"), type_exp.name);
		}

		if(this.exp instanceof AST_EXP_OPT){
			AST_EXP_OPT constExp = (AST_EXP_OPT) this.exp;
			if (!(constExp.opt.equals("INT") && constExp.i > 0)) {
				throw new SemanticException("Allocating arrays with the new operator, when done with a constant, must be greater then zero",
						this);
			}
		}

		return type_var.type;
	}
		//TODO: should fail on negative index to array?
		//TODO: what should return from here? how do i acess the array[i]? meaning, how was it put in the symbol table?
	}
}
