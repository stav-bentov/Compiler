package AST;
import TYPES.*;
import TEMP.*;
import IR.*;


import java.util.List;


public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> parmeters_list;
    public TYPE_FUNCTION func;

    public AST_STMT_ID(AST_VAR var, String id, AST_LIST<AST_EXP> l, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (var != null && l != null)
            System.out.printf("====================== stmt -> var DOT ID(%s)(commaExpList) SEMICOLON", id);
        else if (var != null && l == null)
            System.out.printf("====================== stmt -> var DOT ID(%s)() SEMICOLON", id);
        else if (var == null && l != null)
            System.out.printf("====================== stmt -> ID(%s)(commaExpList) SEMICOLON", id);
        else
            System.out.printf("====================== stmt ->  ID(%s)() SEMICOLON", id);
        this.var = var;
        this.id = id;
        this.parmeters_list = l;
        this.line = line;
    }

    public void PrintMe()
    {
        System.out.format("stmt id %s\n", id);

        if (var != null)
            var.PrintMe();
        if (parmeters_list != null)
            parmeters_list.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("stmt id %s\n", id));
        if(var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if(parmeters_list != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,parmeters_list.SerialNumber);
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        func = CheckCallToFunc(id, var, parmeters_list);

        return null;
    }

    @Override
    public TEMP IRme() {

        List<TEMP> temp_list = this.parmeters_list == null ? null : build_param_list(this.parmeters_list);

        if (this.var == null)
        {
            /* Functions: 1. PrintString
                          2. PrintInt
                          (For 1 and 2 there is only one parameter = param_temp.head)
                          3. global functions
             */
            if (this.id.equals("PrintString"))
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_String(temp_list.get(0)));
            }
            else if (this.id.equals("PrintInt"))
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_Int(temp_list.get(0)));
            }
            else if (func.isMethod)
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, func.offset));
            }
            else // Global func
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func(temp_list, func.func_label));
            }
        }
        else
        {
            /* this.var is a class instance, were trying to access its VT
		       this.var.IRme will return the class pointer, which hase VT ptr in offset 0 */
            TEMP classPtr = this.var.IRme();

            IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, func.offset, classPtr));
        }
        return null;
    }
}
