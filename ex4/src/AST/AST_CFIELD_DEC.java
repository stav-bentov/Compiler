package AST;
import SYMBOL_TABLE.*;
import TEMP.TEMP;
import TYPES.*;

import java.util.HashMap;
import java.util.Map;

public class AST_CFIELD_DEC<T extends AST_Node> extends AST_CFIELD{
    public T dec;
    public String type;

    public AST_CFIELD_DEC(T dec, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (dec instanceof AST_VAR_DEC) {
            this.type = "varDec";
        }
        else if (dec instanceof AST_FUNC_DEC) {
            this.type = "funcDec";
        }
        System.out.format("====================== cField -> %s\n", this.type);
        this.dec = dec;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("cfield: %s\n", this.type);

        if(dec != null)
            dec.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("cfield: %s", type));
        if (dec != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        TYPE dataMemberToAdd = dec.SemantMe();

        TYPE_CLASS currClass = SYMBOL_TABLE.getInstance().getCurrentClass();

        /* Add new data member to data_members of current class */
        TYPE_LIST dataMembers = currClass.data_members;
        currClass.data_members = new TYPE_LIST(dataMemberToAdd, dataMembers);


        /* Updates data members including inherited for VT purposes later */
        /* Add new data members/overrides */
        // If inherited, the member's name is already in the map but will be overridden according to put's contract
        // If not inherited, will be added to the map according to puts implementation
        System.out.println("adding " + dataMemberToAdd.name + " to " + currClass.name);

        if(currClass.data_members_including_inherited.containsKey(dataMemberToAdd.name)){
            TYPE parentDataMember = currClass.data_members_including_inherited.get(dataMemberToAdd.name);
            if(parentDataMember instanceof TYPE_VAR && dataMemberToAdd instanceof TYPE_VAR){
                ((TYPE_VAR) dataMemberToAdd).var_offset = ((TYPE_VAR) parentDataMember).var_offset;
            }
            if(parentDataMember instanceof TYPE_FUNCTION && dataMemberToAdd instanceof TYPE_FUNCTION){
                ((TYPE_FUNCTION) dataMemberToAdd).offset = ((TYPE_FUNCTION) parentDataMember).offset;
            }
            //according to SemantMe rest of cases are not possible
        }

        currClass.data_members_including_inherited.put(dataMemberToAdd.name, dataMemberToAdd);
        System.out.println(currClass.name + ": " + currClass.data_members_including_inherited);

        return null;
    }

    @Override
    public TEMP IRme() {
        return dec.IRme();
    }
}
