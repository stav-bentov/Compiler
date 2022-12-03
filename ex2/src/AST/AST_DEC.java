package AST;

public class AST_DEC extends AST_Node
{
	public AST_VARDEC vardec;
	public AST_FUNCDEC funcdec;
	public AST_CLASSDEC classdec;
	public AST_ARRAYTYPEDEF arraytypedef;

	public AST_DEC(AST_VARDEC vardec, AST_FUNCDEC funcdec, AST_CLASSDEC classdec, AST_ARRAYTYPEDEF arraytypedef){
		this.vardec = vardec;
		this.funcdec = funcdec;
		this.classdec = classdec;
		this.arraytypedef = arraytypedef;
		SerialNumber = AST_Node_Serial_Number.getFresh();
	}

}
