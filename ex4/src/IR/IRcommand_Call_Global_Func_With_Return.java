package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Global_Func_With_Return extends IRcommand
{
    private TEMP_LIST param_list;
    private String func_prolog_label;
    private TEMP assigned_temp;

    /* This IRcommand represent: assigned_temp = func(param_list); ---- with assigning return value to creation register*/
    public IRcommand_Call_Global_Func_With_Return(String func_prolog_label, TEMP_LIST param_list, TEMP assigned_temp) {
        this.func_prolog_label = func_prolog_label;
        this.param_list = param_list;
        this.assigned_temp = assigned_temp;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().set_arguments(this.param_list);
        MIPSGenerator.getInstance().jump(this.func_prolog_label);
        MIPSGenerator.getInstance().jump(this.func_prolog_label);
        MIPSGenerator.getInstance().del_arguments(this.param_list.len);
    }
}
