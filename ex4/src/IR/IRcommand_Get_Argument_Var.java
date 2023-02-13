/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

/*******************/

public class IRcommand_Get_Argument_Var extends IRcommand
{
	public int var_offset;
	public TEMP var_temp;

	/* This IRcommand represent: var_temp <- $fp[var_offset] */
	public IRcommand_Get_Argument_Var(int var_offset, TEMP var_temp)
	{
		this.var_offset = var_offset;
		this.var_temp = var_temp;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().get_var_with_offset(this.var_offset, this.var_temp);
	}
}
