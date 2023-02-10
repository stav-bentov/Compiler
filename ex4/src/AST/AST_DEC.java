package AST;
import TEMP.TEMP;
import TYPES.*;

public class AST_DEC<T extends AST_Node> extends AST_Node
{
	public String type;
	public T dec;

	public AST_DEC(T dec, int line){
		SerialNumber = AST_Node_Serial_Number.getFresh();
		if(dec instanceof AST_VAR_DEC) type = "varDec";
		if(dec instanceof AST_FUNC_DEC) type = "funcDec";
		if(dec instanceof AST_CLASS_DEC) type = "classDec";
		if(dec instanceof AST_ARRAY_TYPEDEF) type = "arrayTypedef";
		System.out.format("====================== dec -> %s\n", type);
		this.dec = dec;
		this.line = line;
	}

	public void PrintMe() {
		System.out.format("dec\n");

		if (dec != null)
			dec.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("dec<%s>", type));
		if (dec != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException
	{
		/* Assumption- the enter occur in SemantMe()*/
		this.dec.SemantMe();
		return null;
	}

	@Override
	public TEMP IRme()
	{
		return this.dec.IRme();
	}
}
