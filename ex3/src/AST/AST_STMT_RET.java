package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RET extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RET(AST_EXP exp) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        String addedExp = "";
        if (exp != null) {
            addedExp = " exp";
        }
        String deriveRule = String.format("====================== stmt -> RETURN%s SEMICOLON\n", addedExp);
        System.out.print(deriveRule);
        this.exp = exp;
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
    public TYPE SemantMe() {
        TYPE expectedReturnType = ((TYPE_FUNCTION)SYMBOL_TABLE.getInstance().getLastFunc()).returnType;

        if (exp == null) { // Returns nothing, return type should be void
             return SemantMeCaseVoid(expectedReturnType);
        }

        return SemantMeCaseNonVoid(expectedReturnType);
    }

    private TYPE SemantMeCaseVoid(TYPE expectedReturnType) {
        if (expectedReturnType instanceof TYPE_VOID) {
            throw new SemanticException(
                    "Returns nothing when expected return type isn't void",
                    this
            );
        }
        return null;
    }

    /* In case the return statment is not empty, we need to check if the exp matches the return type:
    *   a. expectedReturnType shouldn't be void because we return something
    *   b. expectedReturnType should be either string, int, a class, an array:
    *       * class should allow inheritance
    *       * array should be checked to be the same array (TYPE_ARRAY can be instanced)
    *       all should be checked to match the actual return type  */
    private TYPE SemantMeCaseNonVoid(TYPE expectedReturnType) {
        if (expectedReturnType instanceof TYPE_VOID) {
            throw new SemanticException(
                    "Returns something when expected return type is void",
                    this
            );
        }

        TYPE returnType = exp.SemantMe();

        /* Both classes */
        if (expectedReturnType instanceof TYPE_CLASS && returnType instanceof TYPE_CLASS &&
            ((TYPE_CLASS)returnType).inheritsFrom((TYPE_CLASS)expectedReturnType)) {
            return null;
        }

        /* Both arrays */
        if (expectedReturnType instanceof TYPE_ARRAY && returnType instanceof TYPE_ARRAY &&
                returnType.equals(expectedReturnType)) {
            return null;
        }

        /* Only cases left for the expected return type are string or int,
        just need to make sure the actual return type matches them */
        if (returnType.getClass().equals(expectedReturnType.getClass())) {
            return null;
        }

        /* None of the above */
        throw new SemanticException(
                "Return type does not match expected return type",
                this
        );
    }
}
