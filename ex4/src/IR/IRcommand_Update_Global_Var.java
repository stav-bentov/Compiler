/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Update_Global_Var extends IRcommand
{
	public String global_var_label;
	public TEMP temp_to_assign;

	public IRcommand_Update_Global_Var(String global_var_label, TEMP temp_to_assign)
	{
		this.global_var_label = global_var_label;
		this.temp_to_assign = temp_to_assign;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().update_global_var(this.global_var_label, this.temp_to_assign);
	}
}
