package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Global_Func_No_Return extends IRcommand
{
    private TEMP_LIST param_list;
    private String func_prolog_label;

    /* This IRcommand represent: func(param_list); ---- No passing return value to creation register*/
    public IRcommand_Call_Global_Func_No_Return(String func_prolog_label, TEMP_LIST param_list) {
        this.func_prolog_label = func_prolog_label;
        this.param_list = param_list;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().set_arguments(this.param_list);
        MIPSGenerator.getInstance().jump(this.func_prolog_label);
        MIPSGenerator.getInstance().del_arguments(this.param_list.len);
    }
}
