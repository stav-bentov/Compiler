package AST;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> parmeters_list;
    public String func_prologe_label;

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
        CheckCallToFunc(id, var, parmeters_list);
        String class_name = "";

        /* TODO: Lilach take care of class_name*/
        if (this.var != null)
        {
            /*....*/
        }

        /* Set func_label*/
        if (!this.id.equals("main"))
        {
            this.func_prologe_label = "start_" + this.id + "_" + class_name;
        }
        else
        {
            this.func_prologe_label = this.id;
        }

        return null;
    }

    @Override
    public TEMP IRme() {

        TEMP_LIST temp_list = build_param_list(this.parmeters_list);

        if (this.var == null)
        {
            /* Functions: 1. PrintString
                          2. PrintInt
                          (For 1 and 2 there is only one parameter = param_temp.head)
                          3. global functions
             */
            if (this.id.equals("PrintString"))
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_String(temp_list.head));
            }
            else if (this.id.equals("PrintInt"))
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Print_Int(temp_list.head));
            }
            else
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func_No_Return(this.func_prologe_label, temp_list));
            }
        }
        else
        {/* TODO: For Lilach take care of methods */

        }
        return null;
    }
}
