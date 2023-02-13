package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Call_Print_String extends IRcommand
{
    private TEMP print_temp;

    public IRcommand_Call_Print_String(TEMP print_temp) {
        this.print_temp = print_temp;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().print_string(this.print_temp);
    }
}
