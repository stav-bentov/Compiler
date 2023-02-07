/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.IRcommand;
import TEMP.*;

enum SegmentType{
	NONE,
	DATA,
	CODE
}

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;
	private SegmentType current_segment = SegmentType.NONE;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}

	/*==================================== Stav ====================================*/
	public void lstr(TEMP t,String str, String str_label)
	{
		/* Ceate string value as a global variable in data*/
		open_segment(SegmentType.DATA);
		fileWriter.format("%s: .asciiz %s\n", str_label, str);

		/* TODO: if t!= null */
		/* Point to the seted string*/
		open_segment(SegmentType.CODE);
		fileWriter.format("\tla $t%d, %s\n", t.getRegisterSerialNumber(), str_label);
	}

	public void open_segment(SegmentType segment_type)
	{
		String segment = "data";
		if (segment_type == SegmentType.CODE)
		{
			segment = "code";
		}

		if (this.current_segment != segment_type)
		{
			fileWriter.format(".%s\n", segment);
		}
		this.current_segment = segment_type;
	}

	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}

	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
	}

	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);
	}

	public void li(TEMP t,int value)
	{
		open_segment(SegmentType.CODE);

		int idx=t.getRegisterSerialNumber();

		fileWriter.format("\tli $t%d,%d\n", idx, value);
	}

	private void li(String register, int value) {
		fileWriter.format("\tli %s, %d\n", register, value);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tadd t%d, t%d, t%d\n", dstidx, i1, i2);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tsub t%d, t%d, t%d\n", dstidx, i1, i2);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tmul t%d, t%d, t%d\n", dstidx, i1, i2);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tdiv t%d, t%d, t%d\n", dstidx, i1, i2);
	}

	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}

	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}

	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}

	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tbge t%d,t%d,%s\n",i1,i2,label);
	}

	private void bge(String register1, String register2, String label)
	{
		fileWriter.format("\tbge %s, %s, %s\n", register1, register2, label);
	}

	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tbne t%d,t%d,%s\n", i1, i2, label);
	}

	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tbeq t%d,t%d,%s\n",i1,i2,label);
	}

	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getRegisterSerialNumber();
				
		fileWriter.format("\tbeq t%d,$zero,%s\n",i1,label);
	}

	/* Fixes result of binop to match semantics of L language.
	   In case of overflow, will assign max value,
	   In case of underflow, will assign min value.
	 */
	public void standardBinopToLBinop(TEMP t) {
		int MAX_VALUE = 32767;
		int MIN_VALUE = -32768;

		/* Allocate labels */
		String label_check_overflow = IRcommand.getFreshLabel("check_overflow");
		String label_end = IRcommand.getFreshLabel("end");

		/* Assign registers' names to variables */
		String t_register = String.format("$t%d", t.getRegisterSerialNumber());
		String min_value_register = "$s0";
		String max_value_register = "$s1";

		/* Create code */
		li(min_value_register, MIN_VALUE); // $s0 := MIN_VALUE
		li(max_value_register, MAX_VALUE); // $s1 := MAX_VALUE

		bge(t_register, min_value_register, label_check_overflow); // check if t >= MIN_VALUE
		// here t < MIN_VALUE
		li(t_register, MIN_VALUE); // t := MIN_VALUE
		jump(label_end); // it's underflow, of course it's not and overflow

		label(label_check_overflow); // add label
		bge(max_value_register, t_register, label_end); // check if MAX_VALUE >= t
		// here t > MAX_VALUE
		li(t_register, MAX_VALUE); // t := MAX_VALUE

		label(label_end); // add label
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
