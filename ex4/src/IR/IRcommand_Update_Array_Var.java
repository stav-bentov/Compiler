/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Update_Array_Var extends IRcommand
{
	public TEMP array_temp;
	public TEMP index_temp;
	public TEMP temp_to_assign;

	/*  array_temp[index_temp] <- temp_to_assign*/
	public IRcommand_Update_Array_Var(TEMP array_temp, TEMP index_temp, TEMP temp_to_assign)
	{
		this.array_temp = array_temp;
		this.index_temp = index_temp;
		this.temp_to_assign = temp_to_assign;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().update_array(this.array_temp, this.index_temp, this.temp_to_assign);
	}
}
