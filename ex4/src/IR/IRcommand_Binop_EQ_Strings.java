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

public class IRcommand_Binop_EQ_Strings extends IRcommand
{
	public TEMP str1;
	public TEMP str2;
	public TEMP dst;

	public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP t1, TEMP t2)
	{
		this.dst = dst;
		this.str1 = t1;
		this.str2 = t2;

		this.dest = dst.getSerialNumber();
		this.depends_on.add(t1.getSerialNumber());
		this.depends_on.add(t2.getSerialNumber());
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().compare_strings(dst, str1, str2);
	}
}
