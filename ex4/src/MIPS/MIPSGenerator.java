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
	private String zero = "$zero";

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

	public void add_strings(TEMP dst,TEMP str1,TEMP str2)
	{
		String a0 = "$a0";
		String len_str1 = "$s1";
		String len_str2 = "$s0";
		String return_register = "$v0";
		String syscall_num_and_return = "$v0";
		String dst_register = "$t" + dst.getRegisterSerialNumber();
		String copy_pointer = "$s1";

		/* calculate str1's length */
		len(str1);
		/* len of str1 now in len_str1*/
		move(len_str1, return_register);

		/* calculate str2's length */
		len(str2);
		/* len of str1 now in len_str1*/
		move(len_str2, return_register);

		/* Calculate str1's length + str2's length to $a0*/
		add_registers(a0, len_str1, len_str2);
		/* Add null termination to the united length (for allocation)*/
		addu_registers(a0, a0, 1);

		/* Allocate space for concatenated string, the pointer will be at syscall_num*/
		li(syscall_num_and_return, 9);
		fileWriter.format("\tsyscall\n");

		/* Make dst point to the beginning of the concatenated string.*/
		move(dst_register,syscall_num_and_return);

		/* Now we need to build it- copy str1 and then str2,
		* the copy_pointer will help us to copy each str's char in the right place */

		move(copy_pointer, dst_register);
		copy(str1, copy_pointer);
		copy(str2, copy_pointer);

		/* Now copy_pointer points to the end of the string- add null terminator*/
		sb(zero, copy_pointer, 0);

		/* dst = concatenated string */
		fileWriter.format("\tmove $t%d, $s1\n", dst.getRegisterSerialNumber());
	}

	/* Receives strings of dst_register and src_register
	   make the mips instruction: dst_register <- src_register
	 */
	public void move(String dst_register, String src_register)
	{
		fileWriter.format("\tmove %s, %s\n", dst_register, src_register);
	}

	/* Receives strings of dst_register, src_register and offset (for src)
	   make the mips instruction lb: dst_register <- src_register[offset]
	 */
	public void lb(String dst_register, String src_register, int offset)
	{
		/* lb: transfers one byte of data from main memory to a register */
		fileWriter.format("\tlb %s, %d(%s)\n", dst_register, offset, src_register);
	}

	/* Receives strings of dst_register, src_register and offset (for src)
	   make the mips instruction lb: dst_register[offset] <- src_register
	 */
	public void sb(String src_register, String dst_register, int offset)
	{
		/* lb: transfers one byte of data from main memory to a register */
		fileWriter.format("\tsb %s, %d(%s)\n", dst_register, offset, src_register);
	}

	/* Receives strings of reg1, reg2 and jump label
	   make the mips instruction beq: if the data: reg1 == reg2 then jump label
	 */
	public void beq_registers(String dst_register, String src_register, String label)
	{
		fileWriter.format("\tbeq %s, %s, %s\n", label);
	}

	/* Receives strings of dst_register, reg and int inc
	   make the mips instruction addu: dst_register = (the data) reg + inc
	 */
	public void addu_registers(String dst_register, String reg, int inc)
	{
		fileWriter.format("\taddu %s, %s, %d\n", dst_register, reg, inc);
	}

	/* Receives strings of dst_register, reg and int inc
	   make the mips instruction addu: dst_register = (the data) reg1 + reg2
	 */
	public void add_registers(String dst_register, String reg1, String reg2)
	{
		fileWriter.format("\tadd %s, %s, %s\n", dst_register, reg1, reg2);
	}

	/* Receives Temp of str
	   Returns str's length in $v0
	   NOTE: this function is using $s0
	 */
	public void len(TEMP str)
	{
		/* Body of len_func: using $v0 to calculate the len of the argument */
		String start_label = IRcommand.getFreshLabel("start_len_loop");
		String end_label = IRcommand.getFreshLabel("end_len_loop");

		/* Set registers */
		String str_register = "$t" + str.getRegisterSerialNumber();
		String len = "$v0";
		String str_pointer = "$s0";

		move(len, zero);
		/* Add start label and check term for end loop */
		fileWriter.format("%s:\n", start_label);
		lb(str_pointer, str_register, 0);
		beq_registers(str_pointer, zero, end_label);

		/* Loop body: len += 1, str_pointer += 1 */
		addu_registers(len, len, 1);
		addu_registers(str_pointer, str_pointer, 1);
		jump(start_label);

		/* Add end label */
		fileWriter.format("%s:\n", end_label);
	}

	/* Receives Temp of str
	   Returns str's length in $v0
	   NOTE: this function is using $s0 and $s2
	   ASSUMPTION: $s1 points to the place that the chars need to be copied to
	 */
	public void copy(TEMP str, String dst_pointer)
	{
		/* Body of len_func: using $v0 to calculate the len of the argument */
		String start_label = IRcommand.getFreshLabel("start_copy_loop");
		String end_label = IRcommand.getFreshLabel("end_copy_loop");

		/* Set registers */
		String str_register = "$t" + str.getRegisterSerialNumber();
		String copied_str_char = "$s0";
		String copied_str_pointer = "$s2";

		move(copied_str_pointer, str_register);

		/* Add start label and check term for end loop */
		fileWriter.format("%s:\n", start_label);
		lb(copied_str_char, copied_str_pointer, 0);
		beq_registers(copied_str_char, zero, end_label);

		/* Loop body: [pointer] = copied_str_char, copied_str_pointer, pointer += 1 */
		sb(copied_str_char, dst_pointer, 0);
		addu_registers(dst_pointer, dst_pointer, 1);
		addu_registers(copied_str_pointer, copied_str_pointer, 1);
		jump(start_label);

		/* Add end label */
		fileWriter.format("\t%s:\n", end_label);

	}

	public void create_start_func(String label_name, int local_var_num)
	{
		open_segment(SegmentType.CODE);
		/* Create the label for the new created function*/
		label(label_name);
		/* Make function prologue */
		func_prologue(local_var_num);
	}

	public void create_end_func(String label_name)
	{
		open_segment(SegmentType.CODE);
		/* Create the label for the end of new created function*/
		label(label_name);
		/* Make function prologue */
		func_epilogue();
	}

	public void func_prologue(int local_var_num)
	{
		/* For Us- Based on practice_10 */
		open_segment(SegmentType.CODE);

		/* Store return address in $ra */
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $ra, 0($sp), 4\n");
		/* Backup $fp of called func */
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $fp, 0($sp), 4\n");
		/* Update $fp = $sp */
		fileWriter.format("\tmove $fp, $sp\n");
		/* Backup all 10 temporaries */
		for (int i = 0; i < 10; i++)
		{
			fileWriter.format("\tsubu $sp, $sp, 4\n");
			fileWriter.format("\tsw $t%d, 0($sp)\n");
		}
		/* Save space for local_var_num local variables */
		fileWriter.format("\tsubu $sp, $sp, %d\n", (local_var_num) * WORD_SIZE);
	}

	/* Function Prologue: update $sp, $fp
                         restore %t0,...,$t9
                         jump to $ra*/
	public void func_epilogue()
	{
		/* For Us- Based on practice_10 */
		open_segment(SegmentType.CODE);

		/* Update $sp = $fp */
		fileWriter.format("\tmove $sp, $fp\n");
		/* Restore */
		for (int i = 0; i < 10; i++)
		{
			fileWriter.format("\tlw $t%d, %d($sp)\n", i, (-(i + 1)) * WORD_SIZE);
		}
		/* Restore precious %fp */
		fileWriter.format("\tlw $fp, 0($sp)\n");
		/* Store return address in $ra */
		fileWriter.format("\tlw $ra, 4($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 8\n");
		/* jump to return address */
		fileWriter.format("\tjr $ra\n");
	}

	/* TODO: change TEMP to temp list*/
	public void set_arguments(TEMP parameter)
	{
		open_segment(SegmentType.CODE);
		/* add arguments*/
		fileWriter.format("\tsubu $sp, $sp, %d\n", 4);
		fileWriter.format("\tsw $t%d, 0($sp)\n", parameter.getRegisterSerialNumber());
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
