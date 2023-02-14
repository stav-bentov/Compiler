/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Set_Array extends IRcommand
{
	public TEMP array_size_temp;
	public TEMP array_temp;

	/*  Set array: array_temp = new array[array_size_temp]
	*   array_temp will be a pointer to an allocated space in heap*/
	public IRcommand_Set_Array(TEMP array_temp, TEMP array_size_temp)
	{
		this.array_temp = array_temp;
		this.array_size_temp = array_size_temp;

		this.dest = array_temp;
		this.depends_on.add(array_size_temp);
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocate_array(array_temp, array_size_temp);
	}
}
