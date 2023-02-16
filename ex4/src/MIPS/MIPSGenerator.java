/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.IRcommand;
import TEMP.*;
import TYPES.TYPE_VAR;

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
	private String invalid_ptr_label = "string_invalid_ptr_dref";
	private String access_violation_label = "string_access_violation";
	private String div_by_0_label = "string_illegal_div_by_0";
	private String this_reg = "$s9";
	private String sp = "$sp";
	private String ra = "$ra";
	private String fp = "$fp";

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}

	public void print_string(TEMP t)
	{
		open_segment(SegmentType.CODE);

		int idx=t.getRegisterSerialNumber();
		fileWriter.format("\tmove $a0,$t%d\n",idx);
		printSyscall();
	}

	public void print_int(TEMP t)
	{
		open_segment(SegmentType.CODE);

		int idx=t.getRegisterSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,$t%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		/* "When printing an integer, print an additional space at the end"*/
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void lstr(TEMP t,String str, String str_label)
	{
		/* Create string value as a global variable in data*/
		open_segment(SegmentType.DATA);
		fileWriter.format("%s: .asciiz %s\n", str_label, str);

		/* Point to the seted string*/
		open_segment(SegmentType.CODE);
		fileWriter.format("\tla $t%d, %s\n", t.getRegisterSerialNumber(), str_label);
	}

	public void open_segment(SegmentType segment_type)
	{
		String segment = "data";
		if (segment_type == SegmentType.CODE)
		{
			segment = "text";
		}

		if (this.current_segment != segment_type)
		{
			fileWriter.format(".%s\n", segment);
		}
		this.current_segment = segment_type;
	}

	public void add_strings(TEMP dst,TEMP str1,TEMP str2)
	{
		open_segment(SegmentType.CODE);

		String a0 = "$a0";
		String len_str1 = "$s1";
		String len_str2 = "$s0";
		String return_register = "$v0";
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
		add(a0, len_str1, len_str2);
		/* Add null termination to the united length (for allocation)*/
		addu(a0, a0, 1);

		/* Allocate space for concatenated string, the pointer will be at syscall_num*/
		malloc();

		/* Make dst point to the beginning of the concatenated string.*/
		move(dst_register,return_register);

		/* Now we need to build it- copy str1 and then str2,
		* the copy_pointer will help us to copy each str's char in the right place */
		move(copy_pointer, dst_register);
		copy(str1, copy_pointer);
		copy(str2, copy_pointer);

		/* Now copy_pointer points to the end of the string- add null terminator*/
		sb(zero, copy_pointer, 0);
	}

	/* Receives strings of dst_register and src_register
	   make the mips instruction: dst_register <- src_register
	 */
	public void move(String dst_register, String src_register)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tmove %s, %s\n", dst_register, src_register);
	}

	public void move(TEMP dst_register, TEMP src_register)
	{
		open_segment(SegmentType.CODE);

		String dst = "$t" + dst_register.getRegisterSerialNumber();
		String src = "$t" + src_register.getRegisterSerialNumber();

		move(dst, src);
	}

	/* Receives strings of dst_register, src_register and offset (for src)
	   make the mips instruction lb: dst_register <- src_register[offset]
	 */
	public void lb(String dst_register, String src_register, int offset)
	{
		open_segment(SegmentType.CODE);

		/* lb: transfers one byte of data from main memory to a register */
		fileWriter.format("\tlb %s, %d(%s)\n", dst_register, offset, src_register);
	}

	/* Receives strings of dst_register, src_register and offset (for src)
	   make the mips instruction sb: dst_register[offset] <- src_register
	 */
	public void sb(String src_register, String dst_register, int offset)
	{
		open_segment(SegmentType.CODE);

		/* lb: transfers one byte of data from main memory to a register */
		fileWriter.format("\tsb %s, %d(%s)\n", src_register, offset, dst_register);
	}

	/* Receives strings of reg1, reg2 and jump label
	   make the mips instruction beq: if the data: reg1 == reg2 then jump label
	 */
	public void beq(String reg1, String reg2, String label)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tbeq %s, %s, %s\n", reg1, reg2, label);
	}

	/* Receives strings of reg1, reg2 and jump label
	   make the mips instruction beq: if the data: reg1 != reg2 then jump label
	 */
	public void bne(String reg1, String reg2, String label)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tbne %s, %s, %s\n", reg1, reg2, label);
	}

	/* Receives strings of dst_register, reg and int inc
	   make the mips instruction addu: dst_register = (the data) reg + inc
	 */
	public void addu(String dst_register, String reg, int inc)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\taddu %s, %s, %d\n", dst_register, reg, inc);
	}

	/* Receives strings of dst_register, reg and int inc
	   make the mips instruction addu: dst_register = (the data) reg1 + reg2
	 */
	public void add(String dst_register, String reg1, String reg2)
	{
		open_segment(SegmentType.CODE);

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
		String str_char = "$s0";
		String str_pointer = "$s3";

		move(len, zero);
		move(str_pointer, str_register);
		/* Add start label and check term for end loop */
		label(start_label);
		lb(str_char, str_pointer, 0);
		beq(str_char, zero, end_label);

		/* Loop body: len += 1, str_pointer += 1 */
		addu(len, len, 1);
		addu(str_pointer, str_pointer, 1);
		jump(start_label);

		/* Add end label */
		label(end_label);
	}

	/* Receives Temp of str and dst_pointer (it's register name)
	   Returns str's length in $v0
	   NOTE: this function is using $s0 and $s2
	   ASSUMPTION: $s1 points to the place that the chars need to be copied to
	 */
	public void copy(TEMP str, String dst_pointer)
	{
		open_segment(SegmentType.CODE);
		/* Body of len_func: using $v0 to calculate the len of the argument */
		String start_label = IRcommand.getFreshLabel("start_copy_loop");
		String end_label = IRcommand.getFreshLabel("end_copy_loop");

		/* Set registers */
		String str_register = "$t" + str.getRegisterSerialNumber();
		String copied_str_char = "$s0";
		String copied_str_pointer = "$s2";

		move(copied_str_pointer, str_register);
		/* Add start label and check term for end loop */
		label(start_label);
		lb(copied_str_char, copied_str_pointer, 0);
		beq(copied_str_char, zero, end_label);

		/* Loop body: [pointer] = copied_str_char, copied_str_pointer, pointer += 1 */
		sb(copied_str_char, dst_pointer, 0);
		addu(dst_pointer, dst_pointer, 1);
		addu(copied_str_pointer, copied_str_pointer, 1);
		jump(start_label);

		/* Add end label */
		label(end_label);
	}


	/* Receives Temp of str and dst_pointer (it's register name)
	   Returns 0 in dst if str1 != str2 OR 1 in dst if str1 == str2*/
	public void compare_strings(TEMP dst, TEMP str1, TEMP str2)
	{
		open_segment(SegmentType.CODE);

		/* Body of len_func: using $v0 to calculate the len of the argument */
		String start_label = IRcommand.getFreshLabel("start_compare_loop");
		String set_dst_0 = IRcommand.getFreshLabel("assign_zero");
		String end_label = IRcommand.getFreshLabel("end_compare_loop");

		/* Set registers */
		String dst_register = "$t" + dst.getRegisterSerialNumber();
		String str1_register = "$t" + str1.getRegisterSerialNumber();
		String str2_register = "$t" + str2.getRegisterSerialNumber();

		String pointer_str1 = "$s0";
		String char_str1 = "$s1";
		String pointer_str2 = "$s2";
		String char_str2 = "$s3";

		/* Set the value of dst to 1, if we will find out a non matching letter- will set it to 0 and exit*/
		li(dst_register, 1);

		/* Set pointers for loops */
		move(pointer_str1, str1_register);
		move(pointer_str2, str2_register);

		/* Add start label and check term for end loop */
		label(start_label);
		lb(char_str1, pointer_str1, 0);
		lb(char_str2, pointer_str2, 0);
		/* If str1[i] != str2[i] -> set dst to 0*/
		bne(char_str1, char_str2, set_dst_0);
		/* Else- if (str1[i] == str2[i]) and str1[i] == null- end */
		beq(char_str1, zero, end_label);
		/* Else- keep going */
		addu(pointer_str1, pointer_str1, 1);
		addu(pointer_str2, pointer_str2, 1);
		jump(start_label);

		/* Add set_dst_0 label and set dst to 0 */
		label(set_dst_0);
		li(dst_register, 0);

		/* Add end label */
		label(end_label);
	}

	public void func_prologue(String prologue_label, int local_var_num)
	{
		System.out.println("in func_prologue of " + prologue_label);
		/* For Us- Based on practice_10 */
		open_segment(SegmentType.CODE);
		/* Create the label for the new created function*/
		label(prologue_label);

		/* Make function prologue */
		/* Store return address in $ra */
		subu(sp, sp, 4);
		store(ra, sp,0);
		/* Backup $fp of called func */
		subu(sp, sp, 4);
		store(fp, sp, 0);
		/* Update $fp = $sp */
		move(fp, sp);
		/* Backup all 10 temporaries */
		for (int i = 0; i < 10; i++)
		{
			subu(sp, sp, 4);
			store("$t" + i, sp, 0);
		}
		/* Save space for local_var_num local variables */
		subu(sp, sp, (local_var_num) * WORD_SIZE);
	}

	/* Receives return register (return_temp) and set $v0 <- return_temp*/
	public void set_v0(TEMP return_temp)
	{
		open_segment(SegmentType.CODE);

		move("$v0", "$t" + return_temp.getRegisterSerialNumber());
	}

	/* Receives return register (assigned_temp) and set assigned_temp <- $v0*/
	public void get_v0(TEMP assigned_temp)
	{
		open_segment(SegmentType.CODE);

		move("$t" + assigned_temp.getRegisterSerialNumber(), "$v0");
	}

	/* Function Prologue: update $sp, $fp
                         restore %t0,...,$t9
                         jump to $ra*/
	public void func_epilogue(String epilogue_label)
	{
		/* For Us- Based on practice_10 */
		open_segment(SegmentType.CODE);
		/* Create the label for the end of new created function*/
		label(epilogue_label);

		/* Make function prologue */
		/* Update $sp = $fp */
		move(sp, fp);
		/* Restore */
		for (int i = 0; i < 10; i++)
		{
			load("$t" + i, sp, (-(i + 1)) * WORD_SIZE);
		}
		/* Restore precious %fp */
		load(fp, sp, 0);
		/* Store return address in $ra */
		load(ra, sp, 4);
		addu(sp, sp, 8);
		/* jump to return address */
		fileWriter.format("\tjr $ra\n");
	}

	public void set_arguments(List<TEMP> param_list)
	{
		open_segment(SegmentType.CODE);

		subu(sp, sp, 4 * param_list.size());

		/* set arguments */
		for (int i = param_list.size() - 1; i >= 0; i--)
		{
			store("$t" + param_list.get(i).getRegisterSerialNumber(), sp, 4 * i);
		}
	}

	public void del_arguments(int param_nums)
	{
		open_segment(SegmentType.CODE);

		/* TODO: ask if it matter to use addi or addu*/
		addu(sp, sp, 4 * param_nums);
	}

	/* When class_ptr is null, will use "this" instead */
	public void call_class_method(int method_offset, TEMP class_ptr)
	{
		open_segment(SegmentType.CODE);

		/* Check pointer's validation  */
		if (class_ptr != null) check_valid_pointer(class_ptr);

		String vt = "$s0";
		String class_ptr_reg = (class_ptr == null) ? this_reg : ("$t" + class_ptr.getRegisterSerialNumber());
		String method_ptr_reg = "$s1";

		load(vt, class_ptr_reg, 0); // VT at offset 0 in the runtime object
		load(method_ptr_reg, vt, method_offset);

		jalr(method_ptr_reg);
	}

	private void jalr(String reg) {
		open_segment(SegmentType.CODE);

		fileWriter.format("\tjalr %s\n", reg);
	}

	public void global_var_dec(String global_var_label, String str_value, int int_value)
	{
		open_segment(SegmentType.DATA);

		if (str_value != null)
		{
			fileWriter.format("\t%s: .asciiz %s\n",global_var_label, str_value);
		}
		else
		{
			fileWriter.format("\t%s: .asciiz %d\n",global_var_label, int_value);
		}
	}

	/* Set an uninitialized global variable*/
	public void global_var_dec(String global_var_label)
	{
		open_segment(SegmentType.DATA);

		fileWriter.format("\t%s: .word %d\n",global_var_label, 0);
	}

	public void assign_stack_var(int var_offset, TEMP assigned_temp)
	{
		open_segment(SegmentType.CODE);

		var_dec(var_offset, assigned_temp, "$fp");
	}

	/* When class_ptr is null, will use "this" instead */
	public void assign_field(int var_offset, TEMP val_to_assign, TEMP class_ptr) {
		open_segment(SegmentType.CODE);

		/* Check pointer's validation  */
		if (class_ptr != null) check_valid_pointer(class_ptr);

		/* If field is accessed using an instance of the class, use that instance.
		   Otherwise, it's being accessed from within the class, hence we use "this", which is stored in this_reg (aka $s9) */
		String base_reg = class_ptr == null ? this_reg : "$t" + class_ptr.getRegisterSerialNumber();

		var_dec(var_offset, val_to_assign, base_reg);
	}

	public void check_valid_pointer(TEMP temp)
	{
		String end_error_handle = IRcommand.getFreshLabel("end_pointer_error");

		/* if temp != null (assigned to null or not assigned) then pass over error handler*/
		bne("$t" + temp.getRegisterSerialNumber(), zero, end_error_handle);

		/* Error:
		Print "“Invalid Pointer Dereference”" and then exit*/
		la("$a0", this.access_violation_label);
		printSyscall();
		exitSyscall();

		label(end_error_handle);
	}

	private void var_dec(int var_offset, TEMP val_to_assign, String base_reg) {
		open_segment(SegmentType.CODE);

		if (val_to_assign == null)
		{
			/* Assign zero (no assignment)*/
			store(zero, base_reg, var_offset);
		}
		else
		{
			String val = "$t" + val_to_assign.getRegisterSerialNumber();
			store(val, base_reg, var_offset);
		}
	}

	/* When class_ptr is null, will use "this" instead */
	public void get_field(int offset, TEMP res, TEMP class_ptr) {
		open_segment(SegmentType.CODE);

		/* Check pointer's validation  */
		check_valid_pointer(class_ptr);

		String res_reg = "$t" + res.getRegisterSerialNumber();
		/* If field is accessed using an instance of the class, use that instance.
		   Otherwise, it's being accessed from within the class, hence we use "this", which is stored in this_reg (aka $s9) */
		String base_reg = class_ptr == null ? this_reg : "$t" + class_ptr.getRegisterSerialNumber();

		open_segment(SegmentType.CODE);
		load(res_reg, base_reg, offset);
	}
	/* Receives variable's offset and register to set the variable data in
	*  Called on local variable or arguments (all access from stack)*/
	public void get_var_with_offset(int var_offset, TEMP var_temp)
	{
		open_segment(SegmentType.CODE);

		load("$t" + var_temp.getRegisterSerialNumber(), fp, var_offset);
	}

	/* Receives variable's global label and register to set the global data in*/
	public void get_global_var(String global_var_label, TEMP var_temp)
	{
		open_segment(SegmentType.CODE);

		/* BEFORE CHANGE:
		la("$s0", global_var_label);
		load("$t" + var_temp.getRegisterSerialNumber(), "$s0", 0);*/

		/* AFTER CHANGE:*/
		la("$t" + var_temp.getRegisterSerialNumber(), global_var_label);
	}

	public void update_global_var(String global_var_label, TEMP temp_to_assign)
	{
		open_segment(SegmentType.CODE);

		/* Load address of global_var_label to %s0 */
		la("$s0", global_var_label);
		/* Store temp_to_assign in this address (update global..)*/
		store("$t" + temp_to_assign.getRegisterSerialNumber(), "$s0", 0);
	}

	/* First cell will contain array size, next cells- array cells
	 *  Set array_size_temp to point to the allocated space (the array)*/
	public void allocate_array(TEMP array_size_temp, TEMP array_temp)
	{
		open_segment(SegmentType.CODE);

		String array_size = "$t" + array_size_temp.getRegisterSerialNumber();
		String array_pointer = "$t" + array_temp.getRegisterSerialNumber();
		String a0 = "$a0";
		String v0 = "$v0";
		String four = "$s0";

		addu(array_size, array_size, 1);

		/* ===========Call malloc syscall============*/
		/* Set $a0 to the required allocated size*/
		move(a0, array_size);
		li(four, 4);
		mul(a0, a0, four);

		malloc();

		/* Set array_pointer ($v0 points to the allocated space)*/
		move(array_pointer, v0);

		/* Set array size in first cell */
		store(array_size, array_pointer, 0);
	}

	/* Calls malloc syscall, assumes $a0 contains the desired size to allocate */
	private void malloc() {
		open_segment(SegmentType.CODE);

		/* Set $v0*/
		li("$v0", 9);

		fileWriter.format("\tsyscall\n");
	}

	private void printSyscall() {
		/* Set $v0*/
		li("$v0", 4);
		fileWriter.format("\tsyscall\n");
	}

	private void exitSyscall() {
		/* Set $v0*/
		li("$v0", 10);
		fileWriter.format("\tsyscall\n");
	}

	private void allocateClassRuntimeObject(TEMP classPtr, int numOfFields) {
		open_segment(SegmentType.CODE);

		String ptr = "$t" + classPtr.getRegisterSerialNumber();
		String arg = "$a0";
		String ret_val = "$v0";

		/* Size to allocate:
			+ each field of size 4 -> 4 * numOfFields
			+ store vt ptr -> 4
		 */
		int size = 4 * numOfFields + 4;
		li(arg, size);

		malloc();

		move(ptr, ret_val);
	}

	private void fillClassRuntimeObject(TEMP classPtr, List<TYPE_VAR> fields, String VTLabel) {
		String obj_ptr = "$t" + classPtr.getRegisterSerialNumber();
		String s0 = "$s0";
		String constToStore = "$s1";

		int offset = 0;

		/* Pointer to VT at offset 0 */
		la(s0, VTLabel);
		store(s0, obj_ptr, offset);
		offset += 4;

		for (TYPE_VAR field : fields) {
			/* Field is initialized with a constant int */
			if (field.initial_cfield_int_value > -1) {
				li(constToStore, field.initial_cfield_int_value);
				store(constToStore, obj_ptr, offset);
			}
			/* Field is initialized with a constant string */
			else if (field.initial_cfield_str_value != null) {
				// TODO: when we figure out how to use strings
			}
			/* Field is not initialized/ is initialized to nil */
			else {
				store(zero, obj_ptr, offset);
			}
			offset += 4;
		}
	}

	public void la(String reg, String label) {
		open_segment(SegmentType.CODE);

		fileWriter.format("\tla %s, %s\n", reg, label);
	}

	public void createClassRuntimeObject(TEMP classPtr, List<TYPE_VAR> fields, String VTLabel) {
		open_segment(SegmentType.CODE);

		allocateClassRuntimeObject(classPtr, fields.size());
		fillClassRuntimeObject(classPtr, fields, VTLabel);
	}

	public void access_array(TEMP array_temp, TEMP array_index_temp, TEMP array_access_temp)
	{
		String absolute_address = "$s0";
		String result_register = "$t" + array_access_temp.getRegisterSerialNumber();

		get_array_cell(array_temp, array_index_temp, absolute_address);

		/* Access - Save the value in result_register*/
		load(result_register, absolute_address, 0);
	}

	/* Receives array_temp (pointer to the start of the array), and array_index_temp (required index)
	   Make absolute_address contain the absolute address of the required cell*/
	public void get_array_cell(TEMP array_temp, TEMP array_index_temp, String absolute_address)
	{
		open_segment(SegmentType.CODE);

		check_oob(array_temp, array_index_temp);
		String array_register = "$t" + array_temp.getRegisterSerialNumber();
		String index_register = "$t" + array_index_temp.getRegisterSerialNumber();
		String four = "$s1";

		/* Array cells are located one cell next (because first cell saves array size */
		move(absolute_address, index_register);
		/* absolute_address will contain the address of the required cell:
		 		1. add 1 to the required index
		 		2. multiply by 4
		 		3. add array address*/
		addu(absolute_address, absolute_address, 1);
		li(four, 4);
		/* absolute_address*=4 */
		mul(absolute_address, absolute_address, four);
		/* absolute_address = absolute_address + array address */
		add(absolute_address, absolute_address, array_register);
	}

	public void update_array(TEMP array_temp, TEMP array_index_temp, TEMP temp_to_assign)
	{
		open_segment(SegmentType.CODE);

		String absolute_address = "$s0";
		String assigned_register = "$t" + temp_to_assign.getRegisterSerialNumber();

		get_array_cell(array_temp, array_index_temp, absolute_address);

		/* Access - Save the value in result_register*/
		store(assigned_register, absolute_address, 0);
	}

	public void check_oob(TEMP array_temp, TEMP array_index_temp)
	{
		String start_check_oob = IRcommand.getFreshLabel("start_check_oob");
		String end_check_oob = IRcommand.getFreshLabel("end_check_oob");
		String error_check_oob = IRcommand.getFreshLabel("error_check_oob");

		String temp_size = "$s0";
		String array_pointer = "$t" + array_temp.getRegisterSerialNumber();
		String array_index = "$t" + array_index_temp.getRegisterSerialNumber();

		label(start_check_oob);
		/* The size of an array is saved in first cell- then check if not 0 <= array_index_temp < size -> error*/
		load(temp_size, array_pointer, 0);
		/* 0 > array_index_temp*/
		blt(array_index, zero, error_check_oob);
		/* Here: 0 <= array_index_temp, if array_index_temp < size then- end check!*/
		blt(array_index, temp_size, end_check_oob);

		/* Else- if array_index_temp >= size then error_check_oob */
		label(error_check_oob);
		/* Print "Access Violation" and then exit*/
		fileWriter.format("\tla $a0, %s\n",this.access_violation_label);
		printSyscall();
		exitSyscall();

		label(end_check_oob);
	}

	public void load(String dst,String src, int offset)
	{
		open_segment(SegmentType.CODE);
		fileWriter.format("\tlw %s, %d(%s)\n",dst, offset, src);
	}

	public void store(String src, String dst, int offset)
	{
		open_segment(SegmentType.CODE);
		fileWriter.format("\tsw %s, %d(%s)\n",src, offset, dst);
	}

	public void store(TEMP src, TEMP dst, int offset)
	{
		open_segment(SegmentType.CODE);

		String src_reg = "$t" + src.getRegisterSerialNumber();
		String dst_reg = "$t" + dst.getRegisterSerialNumber();

		store(src_reg, dst_reg, offset);
	}

	public void li(TEMP t,int value)
	{
		open_segment(SegmentType.CODE);

		int idx=t.getRegisterSerialNumber();

		fileWriter.format("\tli $t%d,%d\n", idx, value);
	}

	private void li(String register, int value) {
		open_segment(SegmentType.CODE);

		fileWriter.format("\tli %s, %d\n", register, value);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tadd $t%d, $t%d, $t%d\n", dstidx, i1, i2);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tsub $t%d, $t%d, $t%d\n", dstidx, i1, i2);
	}

	public void subu(String dst,String oprnd,int sub_val)
	{
		open_segment(SegmentType.CODE);
		fileWriter.format("\tsubu %s, %s, %d\n", dst, oprnd, sub_val);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tmul $t%d, $t%d, $t%d\n", dstidx, i1, i2);
	}

	public void mul(String dst,String oprnd1,String oprnd2)
	{
		open_segment(SegmentType.CODE);
		fileWriter.format("\tmul %s, %s, %s\n", dst, oprnd1, oprnd2);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		open_segment(SegmentType.CODE);

		check_zero_div(oprnd2);

		int i1 = oprnd1.getRegisterSerialNumber();
		int i2 = oprnd2.getRegisterSerialNumber();
		int dstidx = dst.getRegisterSerialNumber();

		fileWriter.format("\tdiv $t%d, $t%d, $t%d\n", dstidx, i1, i2);
	}

	public void check_zero_div(TEMP temp)
	{
		String end_error_handle = IRcommand.getFreshLabel("end_div_error");

		/* if temp != 0 then pass over error handler*/
		bne("$t" + temp.getRegisterSerialNumber(), zero, end_error_handle);

		/* Error:
		Print "Illegal Division By Zero" and then exit*/
		la("$a0", this.div_by_0_label);
		printSyscall();
		exitSyscall();

		label(end_error_handle);
	}

	/* This func is for Text (Code) labels */
	public void label(String inlabel)
	{
		if (this.current_segment != SegmentType.CODE)
		{
			open_segment(SegmentType.CODE);
		}
		fileWriter.format("%s:\n",inlabel);
	}

	public void jump(String inlabel)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tj %s\n",inlabel);
	}

	public void jal(String inlabel)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tjal %s\n", inlabel);
	}

	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();

		fileWriter.format("\tblt $t%d, $t%d, %s\n",i1,i2,label);
	}

	public void blt(String register1, String register2, String label)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tblt %s, %s, %s\n",register1, register2, label);
	}

	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();

		fileWriter.format("\tbgt $t%d, $t%d,%s\n",i1,i2,label);
	}

	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();

		fileWriter.format("\tbge $t%d, $t%d,%s\n",i1,i2,label);
	}

	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();

		fileWriter.format("\tble $t%d,$t%d,%s\n",i1,i2,label);
	}

	private void bge(String register1, String register2, String label)
	{
		open_segment(SegmentType.CODE);

		fileWriter.format("\tbge %s, %s, %s\n", register1, register2, label);
	}

	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tbne $t%d,$t%d,%s\n", i1, i2, label);
	}

	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
		int i2 =oprnd2.getRegisterSerialNumber();
		
		fileWriter.format("\tbeq $t%d,$t%d,%s\n",i1,i2,label);
	}

	public void beqz(TEMP oprnd1,String label)
	{
		open_segment(SegmentType.CODE);

		int i1 =oprnd1.getRegisterSerialNumber();
				
		fileWriter.format("\tbeq $t%d,$zero,%s\n",i1,label);
	}

	/* Fixes result of binop to match semantics of L language.
	   In case of overflow, will assign max value,
	   In case of underflow, will assign min value.
	 */
	public void standardBinopToLBinop(TEMP t) {
		open_segment(SegmentType.CODE);

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

	public void GTIntegers(TEMP dst, TEMP t1, TEMP t2) {
		open_segment(SegmentType.CODE);

		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end        = IRcommand.getFreshLabel("end");
		String label_assign_one  = IRcommand.getFreshLabel("assign_one");

		/******************************************/
		/* [2] if (t1> t2) goto label_AssignOne;  */
		/*     assign Zero; */
		/******************************************/
		bgt(t1,t2,label_assign_one);

		li(dst,0);
		jump(label_end);

		label(label_assign_one);
		li(dst,1);

		label(label_end);
	}

	public void LTIntegers(TEMP dst, TEMP t1, TEMP t2) {
		open_segment(SegmentType.CODE);

		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end        = IRcommand.getFreshLabel("end");
		String label_assign_one  = IRcommand.getFreshLabel("assign_one");

		/******************************************/
		/* [2] if (t1< t2) goto label_AssignOne;  */
		/*     assign zero; */
		/******************************************/
		blt(t1,t2,label_assign_one);

		li(dst,0);
		jump(label_end);

		label(label_assign_one);
		li(dst,1);

		label(label_end);
	}

	public void EQIntegers(TEMP dst, TEMP t1, TEMP t2) {
		open_segment(SegmentType.CODE);

		/*******************************/
		/* [1] Allocate 3 fresh labels */
		/*******************************/
		String label_end        = IRcommand.getFreshLabel("end");
		String label_assign_one  = IRcommand.getFreshLabel("assign_one");
		String label_assign_zero = IRcommand.getFreshLabel("assign_zero");

		/******************************************/
		/* [2] if (t1==t2) goto label_AssignOne;  */
		/*     if (t1!=t2) goto label_AssignZero; */
		/******************************************/
		beq(t1,t2,label_assign_one);
		bne(t1,t2,label_assign_zero);

		/************************/
		/* [3] label_AssignOne: */
		/*                      */
		/*         t3 := 1      */
		/*         goto end;    */
		/*                      */
		/************************/
		label(label_assign_one);
		li(dst,1);
		jump(label_end);

		/*************************/
		/* [4] label_AssignZero: */
		/*                       */
		/*         t3 := 1       */
		/*         goto end;     */
		/*                       */
		/*************************/
		label(label_assign_zero);
		li(dst,0);
		jump(label_end);

		/******************/
		/* [5] label_end: */
		/******************/
		label(label_end);
	}

	public void allocateVT(String label_vt, List<String> methodLabels) {
		open_segment(SegmentType.DATA);
		fileWriter.format("%s:\n",label_vt);

		for (String l : methodLabels) {
			globalWord(l);
		}
	}

	private void globalWord(String s) {
		fileWriter.format("\t.word %s\n", s);
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
