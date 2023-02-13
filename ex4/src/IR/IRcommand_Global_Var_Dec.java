/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Global_Var_Dec extends IRcommand
{
	public String global_var_label;
	public String str_value = null;
	public int int_value = 0;
	public boolean is_init;

	public IRcommand_Global_Var_Dec(String global_var_label, String str_value)
	{
		this.is_init = true;
		this.global_var_label = global_var_label;
		this.str_value = str_value;
	}

	public IRcommand_Global_Var_Dec(String global_var_label, int int_value)
	{
		this.is_init = true;
		this.global_var_label = global_var_label;
		this.int_value = int_value;
	}

	public IRcommand_Global_Var_Dec(String global_var_label)
	{
		this.is_init = false;
		this.global_var_label = global_var_label;
	}

	public void MIPSme()
	{
		if(is_init)
		{
			MIPSGenerator.getInstance().global_var_dec(this.global_var_label, this.str_value, this.int_value);
		}
		else
		{
			MIPSGenerator.getInstance().global_var_dec(this.global_var_label);
		}
	}
}
