package AST;

public class AST_DEC<T extends AST_Node> extends AST_Node
{
	public String type;
	public T dec;

	public AST_DEC(T dec){
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if(dec instanceof AST_VAR_DEC) type = "varDec";
		if(dec instanceof AST_FUNC_DEC) type = "funcDec";
		if(dec instanceof AST_CLASS_DEC) type = "classDec";
		if(dec instanceof AST_ARRAY_TYPEDEF) type = "arrayTypedef";
		System.out.format("====================== dec -> %s\n", type);
		this.dec = dec;
	}

	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.format("AST_DEC %s\n", type);

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		dec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
				String.format("dec: %s\n", type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
	}
}
