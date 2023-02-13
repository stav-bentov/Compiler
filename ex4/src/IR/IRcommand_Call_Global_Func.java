package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Global_Func extends IRcommand_Call_Func
{
    private String func_prolog_label;

    /* With return */
    public IRcommand_Call_Global_Func(TEMP_LIST param_list, TEMP assigned_temp, String func_prolog_label) {
        super(param_list, assigned_temp);
        this.func_prolog_label = func_prolog_label;
    }

    /* No return */
    public IRcommand_Call_Global_Func(TEMP_LIST param_list, String func_prolog_label) {
        super(param_list);
        this.func_prolog_label = func_prolog_label;
    }

    @Override
    public void call() {
        MIPSGenerator.getInstance().jal(func_prolog_label);
    }
}
