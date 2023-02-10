/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Update_Stack_Var extends IRcommand
{
	public int var_offset;
	public TEMP temp_to_assign;

	/* Updating variables from stack- argument or local
	*  $fp[var_offset] <- temp_to_assign*/
	public IRcommand_Update_Stack_Var(int var_offset, TEMP temp_to_assign)
	{
		this.var_offset = var_offset;
		this.temp_to_assign = temp_to_assign;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().update_stack_var(this.var_offset, this.temp_to_assign);
	}
}
