package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.List;

public class AST_CLASS_DEC extends AST_Node{
    public String className;
    public String extendsName;
    public AST_LIST<AST_CFIELD> cFieldList;

    public List<String> methodLabels;
    public List<AST_Node> fieldsExps;
    public String VTLabel;

    public AST_CLASS_DEC(String className, String extendsName, AST_LIST<AST_CFIELD> cFieldList, int line){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) extends ID(%s) {cFieldList}\n", className, extendsName);
        this.className = className;
        this.extendsName = extendsName;
        this.cFieldList = cFieldList;
        this.line = line;
    }

    public AST_CLASS_DEC(String className, AST_LIST<AST_CFIELD> cFieldList, int line){
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("====================== classDec -> CLASS ID(%s) {cFieldList}\n", className);
        this.className = className;
        this.cFieldList = cFieldList;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("classDec\n");

        if (extendsName == null)
            System.out.format("classDec: %s\n", className);
        else
            System.out.format("classDec: %s extends: %s\n", className, extendsName);
        if(cFieldList != null)
            cFieldList.PrintMe();

        String nodeName = extendsName == null ?
                String.format("classDec: %s\n", className) : String.format("classDec: %s extends: %s\n", className, extendsName);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, nodeName);
        if (cFieldList != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cFieldList.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        if(SYMBOL_TABLE.getInstance().getCurrentScopeType() != ScopeTypeEnum.GLOBAL){
            throw new SemanticException(this);
        }

        if(SYMBOL_TABLE.getInstance().find(this.className) != null)
            throw new SemanticException(this);

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/

        TYPE_CLASS type_class = new TYPE_CLASS(null, this.className, null);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        if(this.extendsName != null){
            TYPE father = SYMBOL_TABLE.getInstance().find(this.extendsName);
            if(father == null){
                System.out.println(this.line);
                throw new SemanticException(this);
            }
            if(!father.isClass()) {
                throw new SemanticException(this);
            }

            type_class.father = (TYPE_CLASS) father;
        }

        SYMBOL_TABLE.getInstance().enter(className, type_class, true);
        SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.CLASS, type_class);

        //SemantMe will check if illegal inheritance or duplicated names in the same scope
        TYPE cFieldTypes = cFieldList.SemantMe();

        /* Updates labels and field types for IRme/MIPSme later */
        extractLabelsAndFieldTypes((TYPE_LIST) cFieldTypes); // assuming it returns a list
        type_class.field_exps = this.fieldsExps;
        VTLabel = type_class.label_VT; // for IRme

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        return type_class;
    }

    @Override
    public TEMP IRme() {
        IR.getInstance().Add_IRcommand(new IRcommand_ClassDec_Allocate_VT(VTLabel, methodLabels)); //TODO: what to do in case of inheritance?

        cFieldList.IRme();

        return null; // Doesn't create a temp that should be passed back
    }

    private void extractLabelsAndFieldTypes(TYPE_LIST types) {
        TYPE head = types.head;
        TYPE_LIST tail = types.tail;

        if (head == null) {
            return;
        }

        /* case class method */
        if (head instanceof TYPE_FUNCTION) { // add it's label to labels list
            this.methodLabels.add(((TYPE_FUNCTION) head).func_label);
        }

        /* case field */
        else if (head instanceof TYPE_VAR) {
            this.fieldsExps.add(((TYPE_VAR) head).exp);
        }

        extractLabelsAndFieldTypes(tail);
    }
}
