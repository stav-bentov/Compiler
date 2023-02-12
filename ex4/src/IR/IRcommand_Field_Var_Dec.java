package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Field_Var_Dec extends IRcommand{
    TEMP valueToAssign;
    int offset;
    String VTLabel;

    public IRcommand_Field_Var_Dec(int offset, TEMP valueToAssign, String VTLabel) {
        this.offset = offset;
        this.valueToAssign = valueToAssign;
        this.VTLabel = VTLabel;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().field_var_dec(offset, valueToAssign, VTLabel);
    }
}
