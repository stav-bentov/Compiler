package AST;

public class AST_EXP_OPT extends AST_EXP
{
	public Integer i;
	public String s;
	public String opt;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_OPT(Integer i, String s, String opt) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== exp -> %s", opt);
		if (opt.equals("INT") || opt.equals("MINUS INT"))
			System.out.format("(%d)\n", i);
		if(opt.equals("STRING"))
			System.out.format("(%s)\n", s);
		this.i = i;
		this.s = s;
		this.opt = opt;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = AST EXP OPT*/
		/*************************************/
		System.out.print("AST NODE EXP OPT\n");
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, opt);
	}
}
