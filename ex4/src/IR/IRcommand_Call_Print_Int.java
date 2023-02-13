package IR;
import TEMP.*;

import MIPS.MIPSGenerator;

public class IRcommand_Call_Print_Int extends IRcommand
{
    private TEMP print_temp;

    public IRcommand_Call_Print_Int(TEMP print_temp) {
        this.print_temp = print_temp;
        this.depends_on.add(print_temp.getSerialNumber());
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().print_int(this.print_temp);
    }
}
