package AST;
import TYPES.*;

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
	public AST_STMT_ASSIGN(AST_VAR var,T exp, int line)
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
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("stmt assign\n");

		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"stmt assign\n");

		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		TYPE varType = ((TYPE_VAR)var.SemantMe()).type;
		TYPE expType = exp.SemantMe();

		if (varType.checkAssign(expType)) {
			return null;
		}

		throw new SemanticException(this);
	}
}
