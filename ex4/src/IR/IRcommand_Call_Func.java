package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public abstract class IRcommand_Call_Func extends IRcommand
{
    protected TEMP_LIST param_list;
    protected TEMP assigned_temp;

    /* With return */
    public IRcommand_Call_Func(TEMP_LIST param_list, TEMP assigned_temp) {
        this(param_list);
        this.assigned_temp = assigned_temp;
    }

    /* No return */
    public IRcommand_Call_Func(TEMP_LIST param_list) {
        this.param_list = param_list;
    }

    public void MIPSme()
    {
        if (param_list != null) MIPSGenerator.getInstance().set_arguments(this.param_list);
        call();
        if (assigned_temp != null) MIPSGenerator.getInstance().get_v0(this.assigned_temp);
        if (param_list != null) MIPSGenerator.getInstance().del_arguments(this.param_list.len);
    }

    public abstract void call();
}
