package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TYPES.TYPE_VAR;

import java.util.List;

public class IRcommand_Instantiate_Class extends IRcommand{
    public TEMP classPtr;
    public List<TYPE_VAR> fields;
    public String VTLabel;

    public IRcommand_Instantiate_Class(TEMP classPtr, List<TYPE_VAR> fields, String VTLabel) {
        this.classPtr = classPtr;
        this.fields = fields;
        this.VTLabel = VTLabel;

        this.dest = classPtr;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().createClassRuntimeObject(classPtr, fields, VTLabel);
    }
}
