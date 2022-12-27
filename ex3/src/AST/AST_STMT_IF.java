package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_IF extends AST_STMT {
	public AST_EXP cond;
	public AST_LIST<AST_STMT> body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond, AST_LIST<AST_STMT> body, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== stmt -> IF (exp) {stmtList}\n");
		this.cond = cond;
		this.body = body;
		this.line = line;
	}

	public void PrintMe()
	{
		System.out.print("stmt if\n");

		if (cond != null)
			cond.PrintMe();
		if (body != null)
			body.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "stmt if\n");
		if(cond != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		if(body != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		TYPE condType = cond.SemantMe();

		if (!(condType instanceof TYPE_INT)) {
			throw new SemanticException(this);
		}

		/* Begin a new scope */
		SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.IF, null);
		body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}
}