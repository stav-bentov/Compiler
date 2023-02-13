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

public class IRcommand_Int extends IRcommand
{
	TEMP t;
	int value;
	
	public IRcommand_Int(TEMP t, int value)
	{
		this.t = t;
		this.value = value;

		this.dest = t.getSerialNumber();
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().li(t, value);
	}
}
