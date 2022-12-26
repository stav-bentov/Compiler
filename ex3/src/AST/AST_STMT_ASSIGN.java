package AST;

import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_STMT_ASSIGN<T extends AST_Node> extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public T exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,T exp)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		String expType = "";
		if (exp instanceof AST_EXP) {
			expType = "exp";
		} else if (exp instanceof AST_NEW_EXP) {
			expType = "newExp";
		}
		String deriveRule = String.format("====================== stmt -> var ASSIGN %s SEMICOLON\n", expType);
		System.out.print(deriveRule);
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		TYPE varType = var.SemantMe().type;
		TYPE expType = exp.SemantMe();

		/* Class */
		if (varType instanceof TYPE_CLASS) {
			if (expType instanceof TYPE_NIL) { // Case of nil
				return null;
			}

			else if (expType instanceof TYPE_CLASS &&  // Case of inheritance (also covers case of same class)
					((TYPE_CLASS)expType).inheritsFrom((TYPE_CLASS)varType)) {
				return null;
			}
		}

		/* Array */
		else if (varType instanceof TYPE_ARRAY) {
			if (expType instanceof TYPE_NIL) { // Case of nil
				return null;
			}

			else if (expType instanceof TYPE_ARRAY &&
					expType == varType) {
				return null;
			}
		}

		/* Other */
		else if (varType == expType) {
			return null;
		}

		throw new SemanticException(
			"Illegal assignment to var",
			this
		);
	}
}
