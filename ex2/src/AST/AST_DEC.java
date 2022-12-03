package AST;

public class AST_DEC extends AST_Node
{
	public AST_VAR_DEC vardec;
	public AST_FUNC_DEC funcdec;
	public AST_CLASS_DEC classdec;
	public AST_ARRAYTYPEDEF arraytypedef;

	public AST_DEC(AST_VAR_DEC vardec){
		this.vardec = vardec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> varDec\n");
	}

	public AST_DEC(AST_FUNC_DEC funcdec){
		this.funcdec = funcdec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> funcDec\n");
	}

	public AST_DEC(AST_CLASS_DEC classdec){
		this.classdec = classdec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> classDec\n");
	}

	public AST_DEC(AST_ARRAYTYPEDEF arraytypedef){
		this.arraytypedef = arraytypedef;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> arrayTypedef\n");
	}

}
