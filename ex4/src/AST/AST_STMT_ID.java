package AST;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_STMT_ID extends AST_STMT{
    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> parmeters_list;
    public String func_prologe_label;
    public int method_offset;
    public boolean isMethod;

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
        TYPE_FUNCTION func = CheckCallToFunc(id, var, parmeters_list);
        String class_name = "";

        if (func.isMethod) {
            this.method_offset = func.offset;
            this.isMethod = true;
        }

        else {
            /* Set func_label*/
            if (!this.id.equals("main")) {
                this.func_prologe_label = "start_" + this.id + "_" + class_name; //TODO: handle this label
            } else {
                this.func_prologe_label = this.id;
            }
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
            else if (isMethod)
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, method_offset));
            }
            else
            {
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Global_Func(temp_list, func_prologe_label));
            }
        }
        else
        {
            /* this.var is a class instance, were trying to access its VT
		       this.var.IRme will return the class pointer, which hase VT ptr in offset 0 */
            TEMP classPtr = this.var.IRme();

            IR.getInstance().Add_IRcommand(new IRcommand_Call_Class_Method(temp_list, method_offset, classPtr));
        }
        return null;
    }
}
