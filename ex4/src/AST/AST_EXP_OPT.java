package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_OPT extends AST_EXP
{
	public Integer i;
	public String s;
	public String opt;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_OPT(Integer i, String s, String opt, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== exp -> %s", opt);
		if (opt.equals("INT") || opt.equals("MINUS INT"))
			System.out.format("(%d)\n", i);
		if(opt.equals("STRING"))
			System.out.format("(%s)\n", s);

		this.i = i;
		this.s = s;
		this.opt = opt;
		this.line = line;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		System.out.format("exp");
		String str = "";
		if (opt.equals("INT"))
			str = String.format("%d", i);
		if (opt.equals("MINUS INT"))
			str = String.format("-%d", i);
		if (opt.equals("STRING"))
			str = s;
		System.out.format("exp: %s", str);

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("exp: %s", str));
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s: %s",opt,str));
	}

	public TYPE SemantMe() throws SemanticException
	{
		TYPE type = null;
		switch (this.opt)
		{
			case "MINUS INT":
			case "INT":
				type = TYPE_INT.getInstance();
				break;
			case "STRING":
				type = TYPE_STRING.getInstance();
				break;
			case "NIL":
				type = TYPE_NIL.getInstance();
				break;
		}

		return type;
	}
}
