package IR;
import TEMP.*;
import MIPS.MIPSGenerator;

/* This IRcommand move return_temp to $v0*/
public class IRcommand_Set_V0 extends IRcommand
{
    private TEMP return_temp;

    public IRcommand_Set_V0(TEMP return_temp) {
        this.return_temp = return_temp;

        this.depends_on.add(return_temp);
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().set_v0(this.return_temp);
    }
}
