package AST;

public class AST_EXP_OPT extends AST_EXP
{
	public Integer i;
	public String s;
	public int OPT;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_OPT(Integer i, String s, int OPT) {

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (OPT == 0)
			System.out.print("====================== exp -> [-] INT\n");
		else if (OPT == 1)
			System.out.print("====================== exp -> INT\n");
		else if (OPT == 2)
			System.out.print("====================== exp -> STRING\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.i = i;
		this.s = s;
		this.OPT = OPT;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		String sOPT="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		switch(OPT) {
			case 0:
				sOPT = String.format("%f",i);
			case 1:
				sOPT = String.format("%s",s);
		}
		/*************************************/
		/* AST NODE TYPE = AST EXP OPT*/
		/*************************************/
		System.out.print("AST NODE EXP OPT\n");
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("%s", sOPT));
	}
}
