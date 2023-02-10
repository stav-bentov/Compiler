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

public class IRcommand_Local_Var_Dec extends IRcommand
{
	public int var_offset;
	public TEMP assigned_temp;

	/* If assigned_temp != null then this IR represent $fp + offset <- assigned_temp
	   If assigned_temp != null then $fp + offset <- 0 */
	public IRcommand_Local_Var_Dec(int var_offset, TEMP assigned_temp)
	{
		this.assigned_temp = assigned_temp;
		this.var_offset = var_offset;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().local_var_dec(this.var_offset, this.assigned_temp);

	}
}
