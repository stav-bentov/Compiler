package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Assign_Field extends IRcommand{
    TEMP valueToAssign;
    int offset;
    TEMP classPtr;
    public int int_to_assign;
    public String str_to_assign;

    public IRcommand_Assign_Field(int offset, TEMP valueToAssign, TEMP classPtr) {
        this(offset, valueToAssign);
        this.classPtr = classPtr;

        this.depends_on.add(classPtr);
        this.depends_on.add(valueToAssign);
    }

    public IRcommand_Assign_Field(int offset, TEMP valueToAssign) {
        this.offset = offset;
        this.valueToAssign = valueToAssign;

        this.depends_on.add(valueToAssign);
    }

    public IRcommand_Assign_Field(int offset, TEMP classPtr, String str)
    {
        this.offset = offset;
        this.str_to_assign = str;
        this.classPtr = classPtr;
    }

    public IRcommand_Assign_Field(int offset, TEMP classPtr, int i)
    {
        this.offset = offset;
        this.int_to_assign = i;
        this.classPtr = classPtr;
    }

    @Override
    public void MIPSme() {
        /* When classPtr is not initialized it's null, and it means the assign field function will use "this" instead */
        MIPSGenerator.getInstance().assign_field(this.offset, this.valueToAssign, this.classPtr, this.int_to_assign, this.str_to_assign);
    }
}
