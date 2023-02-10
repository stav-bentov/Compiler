package IR;

import MIPS.MIPSGenerator;

public class IRcommand_End_Func extends IRcommand
{
    private String epilogue_func_label;

    public IRcommand_End_Func(String epilogue_func_label) {
        this.epilogue_func_label = epilogue_func_label;
    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().func_epilogue(this.epilogue_func_label);
    }
}
