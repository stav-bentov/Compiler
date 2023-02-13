package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Start_Func extends IRcommand
{
    private String start_func_label;
    private int local_var_num;

    public IRcommand_Start_Func(String start_func_label, int local_var_num) {
        this.start_func_label = start_func_label;
        this.local_var_num = local_var_num;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().func_prologue(this.start_func_label, this.local_var_num);
    }
}
