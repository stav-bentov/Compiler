package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RET extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RET(AST_EXP exp, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        String addedExp = "";
        if (exp != null) {
            addedExp = " exp";
        }
        String deriveRule = String.format("====================== stmt -> RETURN%s SEMICOLON\n", addedExp);
        System.out.print(deriveRule);
        this.exp = exp;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("AST_STMT_RET\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "return stmt\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        TYPE expectedReturnType = ((TYPE_FUNCTION)SYMBOL_TABLE.getInstance().getLastFunc()).returnType;

        if (exp == null) { // Returns nothing, return type should be void
             return SemantMeCaseVoid(expectedReturnType);
        }

        return SemantMeCaseNonVoid(expectedReturnType);
    }

    private TYPE SemantMeCaseVoid(TYPE expectedReturnType) throws SemanticException {
        if (!(expectedReturnType instanceof TYPE_VOID)) {
            throw new SemanticException(this);
        }
        return null;
    }

    /* In case the return statment is not empty, we need to check if the exp matches the return type:
    *   a. expectedReturnType shouldn't be void because we return something
    *   b. expectedReturnType should be either string, int, a class, an array:
    *       * class should allow inheritance
    *       * array should be checked to be the same array (TYPE_ARRAY can be instanced)
    *       all should be checked to match the actual return type  */
    private TYPE SemantMeCaseNonVoid(TYPE expectedReturnType) throws SemanticException {
        if (expectedReturnType instanceof TYPE_VOID) {
            throw new SemanticException(this);
        }

        TYPE returnType = exp.SemantMe();

        /* Returns a class */
        if (expectedReturnType instanceof TYPE_CLASS) {
            /* Inheritance is allowed */
            if (returnType instanceof TYPE_CLASS &&
            ((TYPE_CLASS)returnType).inheritsFrom((TYPE_CLASS)expectedReturnType)) {
                return null;
            }

            /* Nil is allowed */
            if (returnType instanceof TYPE_NIL) {
                return null;
            }
        }

        /* Returns an array */
        else if (expectedReturnType instanceof TYPE_ARRAY) {
            /* Same array */
            if (returnType instanceof TYPE_ARRAY &&
                returnType == expectedReturnType) {
                return null;
            }

            /* Nil is allowed */
            if (returnType instanceof TYPE_NIL) {
                return null;
            }
        }

        /* Only cases left for the expected return type are string or int,
        just need to make sure the actual return type matches them */
        else if (returnType == expectedReturnType) {
            return null;
        }

        /* None of the above */
        throw new SemanticException(this);
    }
}
