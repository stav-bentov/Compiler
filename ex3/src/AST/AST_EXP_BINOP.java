package AST;
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	public String sOP;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP, int line) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		switch(OP) {
			case 0:
				sOP = "+";
				break;
			case 1:
				sOP = "-";
				break;
			case 2:
				sOP = "*";
				break;
			case 3:
				sOP = "/";
				break;
			case 4:
				sOP = "<";
				break;
			case 5:
				sOP = ">";
				break;
			case 6:
				sOP = "=";
				break;
		}
		System.out.print(String.format("====================== exp -> exp %s exp\n", sOP));
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.line = line;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		switch(OP) {
			case 0:
				sOP = "+";
				break;
			case 1:
				sOP = "-";
				break;
			case 2:
				sOP = "*";
				break;
			case 3:
				sOP = "/";
				break;
			case 4:
				sOP = "<";
				break;
			case 5:
				sOP = ">";
				break;
			case 6:
				sOP = "=";
				break;
		}
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE EXP BINOP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException{
		//figuring out the types of left and right using their SemantMe
		TYPE left_type = this.left.SemantMe();
		TYPE right_type = this.right.SemantMe();

		boolean are_same_type = left_type.getClass().equals(right_type.getClass());
		switch (this.sOP){
			case "+":
				// + can only occur between two ints or two strings. gonna check if they are of the same class first
				if(!are_same_type)
					throw new SemanticException(this);

				// Here they are of the same class. we check if left_type is eiter TYPE_INT or TYPE_STRING. if not, then error.
				if(!(left_type instanceof TYPE_INT) && !(left_type instanceof TYPE_STRING))
					throw new SemanticException(this);

				// So left_type is the type we wanna return. if he is TYPE_INT we wil return TYPE_INT and same for TYPE_STRING
				return left_type;

			case "=":

				// Equality testing must happen between two objects of the same type (besides the option of nil and class/array
				if(!are_same_type)
				{
					// Only classes and arrays can be compared to NIL
					boolean leftIsNull = left_type instanceof TYPE_NIL && (right_type instanceof TYPE_CLASS || right_type instanceof TYPE_ARRAY);
					boolean rightIsNull = right_type instanceof TYPE_NIL && (left_type instanceof TYPE_CLASS || left_type instanceof TYPE_ARRAY);
					if (!leftIsNull && !rightIsNull)
						throw new SemanticException(this);
				}
				else //are_same_type
				{
					// If the two objects are classes, we need to check that they are percisly the same class, or inherit from one another
					if(left_type instanceof TYPE_CLASS){
						boolean leftInheritsFromRight = ((TYPE_CLASS) left_type).inheritsFrom((TYPE_CLASS) right_type);
						boolean rightInheritsFromLeft = ((TYPE_CLASS) right_type).inheritsFrom((TYPE_CLASS) left_type);
						if(!leftInheritsFromRight && !rightInheritsFromLeft)
							throw new SemanticException(this);
					}

					//arrays have to be precisely the same. using equals between them to check they are the same array
					if(left_type instanceof TYPE_ARRAY){
						if(!(left_type == right_type))
							throw new SemanticException(this);
					}
				}

				//equality always returns TYPE_INT
				return TYPE_INT.getInstance();

			default:
				//the rest of binary operations (-, *, /, >, <) can happen only between two ints
				if(!(left_type instanceof TYPE_INT) || !(right_type instanceof TYPE_INT))
					throw new SemanticException(this);

				//check that we do not divide by a constant 0
				if(sOP.equals("/") && right instanceof AST_EXP_OPT && ((AST_EXP_OPT) right).i == 0)
					throw new SemanticException(this);
				return TYPE_INT.getInstance();
		}

	}
}
