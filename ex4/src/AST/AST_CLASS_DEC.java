package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.ArrayList;
import java.util.List;

public class AST_CLASS_DEC extends AST_Node{
    public String className;
    public String extendsName;
    public AST_LIST<AST_CFIELD> cFieldList;

    public List<String> methodLabels = new ArrayList<>();

    public TYPE_CLASS typeClass;

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

        typeClass = new TYPE_CLASS(null, this.className, null);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/

        if(this.extendsName != null){
            TYPE father = SYMBOL_TABLE.getInstance().find(this.extendsName);
            if(father == null){
                throw new SemanticException(this);
            }
            if(!father.isClass()) {
                throw new SemanticException(this);
            }

            typeClass.father = (TYPE_CLASS) father;
        }
        typeClass.SetDataMembersIncludingInherited();

        SYMBOL_TABLE.getInstance().enter(className, typeClass, true);
        SYMBOL_TABLE.getInstance().beginScope(ScopeTypeEnum.CLASS, typeClass);

        //SemantMe will check if illegal inheritance or duplicated names in the same scope
        cFieldList.SemantMe();

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        return typeClass;
    }

    @Override
    public TEMP IRme() {
        extractLabelsAndFieldTypes();
        IR.getInstance().Add_IRcommand(new IRcommand_ClassDec_Allocate_VT(this.typeClass.label_VT, methodLabels));

        cFieldList.IRme();

        return null; // Doesn't create a temp that should be passed back
    }

    private void extractLabelsAndFieldTypes() {
        for (TYPE type : this.typeClass.data_members_including_inherited.values()) {
            /* case class method */
            if (type instanceof TYPE_FUNCTION) { // add it's label to labels list
                this.methodLabels.add(((TYPE_FUNCTION) type).func_label);
            }

            /* case field */
            else if (type instanceof TYPE_VAR) {
                this.typeClass.fields.add(((TYPE_VAR) type));
            }
        }
    }
}
