package AST;

public class AST_DEC extends AST_Node
{
	public AST_VARDEC vardec;
	public AST_FUNCDEC funcdec;
	public AST_CLASSDEC classdec;
	public AST_ARRAYTYPEDEF arraytypedef;

	public AST_DEC(AST_VARDEC vardec){
		this.vardec = vardec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> varDec\n");
	}

	public AST_DEC(AST_FUNCDEC funcdec){
		this.funcdec = funcdec;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("====================== dec -> funcDec\n");
	}

	public AST_DEC(AST_CLASSDEC classdec){
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
