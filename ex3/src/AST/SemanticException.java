package AST;

public class SemanticException extends Exception {
    public String description;
    public AST_Node node;

    public SemanticException(String description, AST_Node node) {
        this.description = description;
        this.node = node;
    }
}
