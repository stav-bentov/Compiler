package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Get_Class_Var extends IRcommand{
    String VTLabel;
    int offset;
    TEMP resReg;

    public IRcommand_Get_Class_Var(String VTLabel, int offset, TEMP resReg) {
        this.VTLabel = VTLabel;
        this.offset = offset;
        this.resReg = resReg;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().get_field(offset, VTLabel, resReg);
    }
}
