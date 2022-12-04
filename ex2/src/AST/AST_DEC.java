package AST;

public class AST_DEC<T extends AST_Node> extends AST_Node
{
	public T dec;

	public AST_DEC(T dec){
		this.dec = dec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> %s\n", dec.getClass());
	}
}
