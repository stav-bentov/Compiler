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
import TEMP.*;
import MIPS.*;

/* "beq cond, 0, after_if_body_label"*/
public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand
{
	TEMP cond;
	String after_if_body_label;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP cond, String after_if_body_label)
	{
		this.cond          = cond;
		this.after_if_body_label = after_if_body_label;

		this.depends_on.add(cond);
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().beqz(cond, after_if_body_label);
	}
}
