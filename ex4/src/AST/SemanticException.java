package AST;

public class SemanticException extends Exception {
    public AST_Node node;

    public SemanticException(AST_Node node) {
        this.node = node;
    }
}
