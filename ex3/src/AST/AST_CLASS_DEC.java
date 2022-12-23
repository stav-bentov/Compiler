package AST;
import TYPES.*
import SYMBOL_TABLE.*
import src.AST.AST_LIST;

public class AST_CLASS_DEC extends AST_Node{
    public String className;
    public String extendsName;
    public AST_LIST<AST_CFIELD> cFieldList;

    public AST_CLASS_DEC(String className, String extendsName, AST_LIST<AST_CFIELD> cFieldList){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) extends ID(%s) {cFieldList}\n", className, extendsName);
        this.className = className;
        this.extendsName = extendsName;
        this.cFieldList = cFieldList;
    }

    public AST_CLASS_DEC(String className, AST_LIST<AST_CFIELD> cFieldList){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) {cFieldList}\n", className);
        this.className = className;
        this.cFieldList = cFieldList;
    }

    public void PrintMe() {
        /*********************************/
        /* AST NODE TYPE = AST FIELD VAR */
        /*********************************/
        System.out.print("AST_CLASS_DEC\n");

        /**********************************************/
        /* RECURSIVELY PRINT VAR, then FIELD NAME ... */
        /**********************************************/
        if(cFieldList != null) cFieldList.PrintMe();
        if (extendsName == null) System.out.format("AST_CLASS_DEC %s\n", className);
        else System.out.format("AST_CLASS_DEC %s extends %s\n", className, extendsName);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        String nodeName = extendsName == null ?
                String.format("class_dec %s\n", className) : String.format("class_dec %s extends %s\n", className, extendsName);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, nodeName);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cFieldList.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        if(SYMBOL_TABLE.getInstance().getCurrentScopeType() != ScopeTypeEnum.GLOBAL){
            throw new SemanticException(String.format("class %s declaration in non-global scope", className));
        }

        if(SYMBOL_TABLE.getInstance().findInGlobal(this.className) != null)
            throw new SemanticException(String.format("class %s already exists!", className));

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/


        TYPE_CLASS type_class = new TYPE_CLASS(null, name, null);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/


        TYPE_CLASS father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().findInGlobal(this.extendsName);

        if(this.extendsName != null){
            if(!father.isClass()) {
                throw new SemanticException(String.format("%s is not a class and cannot be extended by %s", this.extendsName, this.className));
            }

            type_class.father = father;
        }

        SYMBOL_TABLE.getInstance().enter(className, type_class);
        SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.CLASS, type_class);

        //SemantMe will check if illegal inheritance or duplicated names in the same scope
        type_class.data_members = cFieldList.SemantMe();

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        return type_class;
    }
}
