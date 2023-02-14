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

public class IRcommand_Array_Index_Access extends IRcommand
{
	public TEMP array_temp;
	public TEMP array_index_temp;
	public TEMP array_access_temp;

	/* This IRcommand represents: array_access_temp <- array_temp[array_index_temp] */
	public IRcommand_Array_Index_Access(TEMP array_temp, TEMP array_index_temp, TEMP array_access_temp)
	{
		this.array_temp = array_temp;
		this.array_index_temp = array_index_temp;
		this.array_access_temp = array_access_temp;

		this.dest = array_access_temp;
		this.depends_on.add(array_temp);
		this.depends_on.add(array_index_temp);
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().access_array(this.array_temp, this.array_index_temp, this.array_access_temp);
	}
}
