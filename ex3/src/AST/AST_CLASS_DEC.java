package AST;

import SYMBOL_TABLE.*
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

    public TYPE SemantMe()
    {
        if(SYMBOL_TABLE.getInstance().getCurrentScope() != 'global'){
            throw new Exception("class declaration in non-global scope");
        }
        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope('class', this.className);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS type_class = new TYPE_CLASS(null, name, null);
        SYMBOL_TABLE.getInstance().type_class = t;

        if(this.extendsName != null){
            if(!SYMBOL_TABLE.getInstance().isClass(this.extendsName)) {
                throw new Exception(String.format("%s is not a class and cannot be extended by %s", this.extendsName, this.className));
            }

            TYPE_CLASS father_type_class = SYMBOL_TABLE.getInstance().find(this.extendsName);
            type_class.father = father_type_class;
            type_class.data_members = (cFieldList.SemantMe()).Merge(father_type_class.data_members);//TODO: add function Merge to TYPE_LIST
            type_class.count_fields = type_class.data_members.len;//TODO: add len to TYPE_LIST
            SYMBOL_TABLE.getInstance().father_type_class = father_type_class;
        }

        else{
            type_class.data_members = SemantMe();
            type_class.count_fields = type_class.data_members.len;//TODO: add len to TYPE_LIST
        }

        //TODO: should register methods of class on symbol table?
        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().type_class = null;
        SYMBOL_TABLE.getInstance().father_type_class = null;
        SYMBOL_TABLE.getInstance().endScope();

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(name,t);//TODO: fix according to implementation of SYMBOL_TABLE.enter

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return type_class;
    }
}
