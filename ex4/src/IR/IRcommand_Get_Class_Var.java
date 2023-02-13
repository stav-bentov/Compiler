package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Get_Class_Var extends IRcommand{
    int offset;
    TEMP classPtr;
    TEMP resReg;

    public IRcommand_Get_Class_Var(int offset, TEMP resReg, TEMP classPtr) {
        this(offset, resReg);
        this.classPtr = classPtr;

        this.dest = resReg.getSerialNumber();
        this.depends_on.add(classPtr.getSerialNumber());
    }

    public IRcommand_Get_Class_Var(int offset, TEMP resReg) {
        this.offset = offset;
        this.resReg = resReg;

        this.dest = resReg.getSerialNumber();
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().get_field(offset, resReg, this.classPtr);
    }
}
