package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;

public class IRcommand_Instantiate_Class extends IRcommand{
    public TEMP classPtr;
    public List<TEMP> initialValueTemps;
    public String VTLabel;

    public IRcommand_Instantiate_Class(TEMP classPtr, List<TEMP> initialValueTemps, String VTLabel) {
        this.classPtr = classPtr;
        this.initialValueTemps = initialValueTemps;
        this.VTLabel = VTLabel;

        this.dest = classPtr;
        this.depends_on.addAll(initialValueTemps);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().createClassRuntimeObject(classPtr, initialValueTemps, VTLabel);
    }
}
