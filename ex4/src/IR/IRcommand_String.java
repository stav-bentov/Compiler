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

public class IRcommand_String extends IRcommand
{
	TEMP t;
	String str;
	String str_label;
	
	public IRcommand_String(TEMP t,String str)
	{
		this.t = t;
		this.str = str;
		this.str_lable = label;
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().lstr(t,value);
	}
}
