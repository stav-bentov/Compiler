package AST;
import IR.IR;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_EXP_OPT extends AST_EXP
{
	public Integer i;
	public String s;
	public String opt;
	public String str_label = null;
	
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

	public TEMP IRme() {
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();

		switch (this.opt)
		{
			case "MINUS INT":
				IR.getInstance().Add_IRcommand(new IRcommand_Int(t, (-1) * this.i));
				break;
			case "INT":
				IR.getInstance().Add_IRcommand(new IRcommand_Int(t, this.i));
				break;
			case "STRING":
				this.str_label = IRcommand.getFreshLabel("str");
				IR.getInstance().Add_IRcommand(new IRcommand_String(t, this.s, str_label));
				break;
			case "NIL":
				IR.getInstance().Add_IRcommand(new IRcommand_Nil(t));
				break;
		}

		return t;
	}
}
