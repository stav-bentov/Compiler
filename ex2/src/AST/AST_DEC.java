package AST;

public class AST_DEC<T extends AST_Node> extends AST_Node
{
	public T dec;

	public AST_DEC(T dec){
		this.dec = dec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> %s\n", dec.toString());
	}

	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.format("AST_DEC %s\n", dec.toString());

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		dec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
				String.format("dec: %s\n", dec.toString()));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
	}
}
