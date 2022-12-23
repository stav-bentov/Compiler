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
        if (!expectedReturnType.equals(TYPE_VOID.getInstance())) {
            throw new SemanticException(
                    "Returns nothing when expected return type isn't void",
                    this
            );
        }
        return null;
    }

    private TYPE SemantMeCaseNonVoid(TYPE expectedReturnType) {
        if (expectedReturnType.equals(TYPE_VOID.getInstance())) {
            throw new SemanticException(
                    "Returns something when expected return type is void",
                    this
            );
        }

        TYPE returnType = exp.SemantMe();
        /* Both ints */
        if (expectedReturnType.equals(TYPE_INT.getInstance()) && returnType.equals(TYPE_INT.getInstance())) {
            return null;
        }

        /* Both strings */
        if (expectedReturnType.equals(TYPE_STRING.getInstance()) && returnType.equals(TYPE_STRING.getInstance())) {
            return null;
        }

        /* Both classes */
        if (expectedReturnType instanceof TYPE_CLASS && returnType instanceof TYPE_CLASS &&
            ((TYPE_CLASS)returnType).inheritsFrom((TYPE_CLASS)expectedReturnType)) {
            return null;
        }

        /* Both arrays */
        if (expectedReturnType instanceof TYPE_ARRAY && returnType instanceof TYPE_ARRAY &&
                (returnType).equals((expectedReturnType)) {
            return null;
        }

        /* None of the above */
        throw new SemanticException(
                "Return type does not match expected return type",
                this
        );
    }
}
