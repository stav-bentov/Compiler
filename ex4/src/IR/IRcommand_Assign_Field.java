package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Assign_Field extends IRcommand{
    TEMP valueToAssign;
    int offset;
    TEMP classPtr;

    public IRcommand_Assign_Field(int offset, TEMP valueToAssign, TEMP classPtr) {
        this(offset, valueToAssign);
        this.classPtr = classPtr;
    }

    public IRcommand_Assign_Field(int offset, TEMP valueToAssign) {
        this.offset = offset;
        this.valueToAssign = valueToAssign;
    }

    @Override
    public void MIPSme() {
        /* When classPtr is not initialized it's null, and it means the assign field function will use "this" instead */
        MIPSGenerator.getInstance().assign_field(offset, valueToAssign, classPtr);
    }
}
