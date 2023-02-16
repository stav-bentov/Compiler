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
	public int int_to_assign;
	public String str_to_assign;

	public IRcommand_Update_Global_Var(String global_var_label, TEMP temp_to_assign)
	{
		this.global_var_label = global_var_label;
		this.temp_to_assign = temp_to_assign;

		this.depends_on.add(temp_to_assign);
	}

	public IRcommand_Update_Global_Var(String global_var_label, int i)
	{
		this.global_var_label = global_var_label;
		this.int_to_assign = i;
	}

	public IRcommand_Update_Global_Var(String global_var_label, String str)
	{
		this.global_var_label = global_var_label;
		this.str_to_assign = str;
	}

	public void MIPSme()
	{
		if (this.temp_to_assign == null)
		{
			/* String or Int*/
			if (this.str_to_assign != null)
			{
				/* String*/
				MIPSGenerator.getInstance().update_global_var(this.global_var_label, this.str_to_assign);
				return;
			}
			else {
				/* Int*/
				MIPSGenerator.getInstance().update_global_var(this.global_var_label, this.int_to_assign);
				return;
			}
		}
		/* temp_to_assign == null or != null or TYPE_NIL*/
		MIPSGenerator.getInstance().update_global_var(this.global_var_label, this.temp_to_assign);
	}
}
