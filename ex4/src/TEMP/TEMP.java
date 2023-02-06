/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP
{
	private int serial = 0;
	private int register_serial = -1;
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}

	public int getSerialNumber()
	{
		return serial;
	}

	public int getRegisterSerialNumber()
	{
		return register_serial;
	}
}
