package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Field_Var_Dec extends IRcommand{
    TEMP valueToAssign;
    TEMP assignedTemp;

    public IRcommand_Field_Var_Dec(TEMP assignedTemp, TEMP valueToAssign) {
        this.assignedTemp = assignedTemp;
        this.valueToAssign = valueToAssign;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().move(assignedTemp, valueToAssign);
    }
}
