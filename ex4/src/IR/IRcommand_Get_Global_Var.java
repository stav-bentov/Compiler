/***********/
/* PACKAGE */
/***********/
package IR;
import TEMP.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import MIPS.MIPSGenerator;

public class IRcommand_Get_Global_Var extends IRcommand
{
	public String global_var_label;
	public TEMP global_temp;

	public IRcommand_Get_Global_Var(String global_var_label, TEMP global_temp)
	{
		this.global_var_label = global_var_label;
		this.global_temp = global_temp;

		this.dest = global_temp;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().get_global_var(this.global_var_label, this.global_temp);
	}
}
