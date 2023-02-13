package IR;

import MIPS.MIPSGenerator;

import java.util.List;

public class IRcommand_ClassDec_Allocate_VT extends IRcommand{
    List<String> methodLabels;
    String VTLabel;

    public IRcommand_ClassDec_Allocate_VT(String VTLabel, List<String> methodLabels) {
        this.VTLabel = VTLabel;
        this.methodLabels = methodLabels;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().allocateVT(VTLabel, methodLabels);
    }
}
