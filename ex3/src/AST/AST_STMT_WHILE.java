package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_STMT_WHILE extends AST_STMT {
	public AST_EXP cond;
	public AST_LIST<AST_STMT> body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_LIST<AST_STMT> body) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== stmt -> WHILE (exp) {stmtList}\n");
		this.cond = cond;
		this.body = body;
	}
	public void PrintMe() {
		System.out.print("AST_STMT_WHILE\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		cond.PrintMe();
		body.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "while stmt\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		TYPE condType = cond.SemantMe();

		if (condType instanceof TYPE_INT) {
			throw new SemanticException(
					"Condition is not an integer",
					this
			);
		}

		body.SemantMe();

		/* Begin a new scope */
		SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.WHILE, null);

		return null;
	}
}