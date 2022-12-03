package AST;

public class AST_STMT_IF extends AST_STMT {
	public AST_EXP cond;
	public AST_LIST<AST_STMT> body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond, AST_LIST<AST_STMT> body) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		// TODO: Add print derivation rule

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
	}
}