package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;
import IR.*;

public class AST_NEW_EXP_NEW_TYPE_EXP extends AST_NEW_EXP {
    public AST_TYPE type;
    public AST_EXP exp;

    public AST_NEW_EXP_NEW_TYPE_EXP(AST_TYPE type, AST_EXP exp, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        String addedExp = "";
        if (exp != null) {
            addedExp = " [ exp ]";
        }
        String deriveRule = String.format("====================== newExp -> NEW type%s\n", addedExp);
        System.out.print(deriveRule);
        this.type = type;
        this.exp = exp;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("exp\n");

        if (type != null)
            type.PrintMe();
        if (exp != null)
            exp.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("stmt new exp \n"));
        if (type != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        TYPE instanceType = type.SemantMe(); // This is a legal type

        /* Make sure instanceType is not void */
        if (instanceType instanceof TYPE_VOID) {
            throw new SemanticException(this);
        }

        /* New instance of an array */
        if (exp != null) {
            return SemantMeCaseArray(instanceType);
        }

        /* New instance of a class */
        else if (instanceType instanceof TYPE_CLASS) {
            return instanceType;
        }

        else {
            throw new SemanticException(this);
        }
    }

    private TYPE SemantMeCaseArray(TYPE arrayMembersType) throws SemanticException {
        /* Check expression */
        TYPE expType = exp.SemantMe();

        /* Check if exp is a constant */
        if (exp instanceof AST_EXP_OPT) {
            AST_EXP_OPT constExp = (AST_EXP_OPT) exp;

            if (!(constExp.opt.equals("INT") && constExp.i > 0)) {
                throw new SemanticException(this);
            }
        }

        /* Check if the expression evaluates to an integer */
        if (!(expType instanceof TYPE_INT)) {
            throw new SemanticException(this);
        }

        return arrayMembersType;
    }

    @Override
    public TEMP IRme()
    {
        TEMP result_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
        /* 2 Cases: 1. array (this.exp!=null)
                    2. class
         */
        if (this.exp != null)
        {
            TEMP array_size_temp = this.exp.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Set_Array(result_temp, array_size_temp));
        }
        else
        {
            /* TODO: For Lilach*/
        }
        return result_temp;
    }
}
