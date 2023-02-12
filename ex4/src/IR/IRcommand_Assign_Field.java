package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Assign_Field extends IRcommand{
    TEMP valueToAssign;
    int offset;
    String VTLabel;

    public IRcommand_Assign_Field(int offset, TEMP valueToAssign, String VTLabel) {
        this.offset = offset;
        this.valueToAssign = valueToAssign;
        this.VTLabel = VTLabel;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().assign_field(offset, valueToAssign, VTLabel);
    }
}
