/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;
	private TYPE_FOR_SCOPE_BOUNDARIES current_scope_boundary = null;

	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, TYPE t, boolean canBeInstanced)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];

		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e;
		if (this.current_scope_boundary != null)
			 e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++, canBeInstanced, current_scope_boundary.scope_type_enum);
		else
			e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++, canBeInstanced, ScopeTypeEnum.GLOBAL);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	private TYPE findNotGlobal(String name){
		SYMBOL_TABLE_ENTRY e;

		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if(e.scopeEnum == ScopeTypeEnum.GLOBAL) break;
			if (name.equals(e.name))
			{
				return e.type;
			}
		}

		return null;
	}

	//first searches in not global (nesting func/class), then inheritance and finally global scope.
	public TYPE find(String name){
		TYPE type;
		type = findNotGlobal(name); // Up until and including class / global func

		if(type == null) {
			type = findInInheritance(name);
		}

		if(type == null){
			SYMBOL_TABLE_ENTRY e = findInGlobal(name);
			type = e != null ? e.type : null;
		}
		return type;
	}

	/* Returns a type that can be instanced, else- returns null */
	public TYPE findInstanced(String name){
		TYPE type = null;
		type = findNotGlobal(name); // Up until and including class / global func

		if(type == null) {
			type = findInInheritance(name);
		}
		else {
			return null; // Found *not* in global -> cannot be instanced
		}

		if(type == null){
			SYMBOL_TABLE_ENTRY e = findInGlobal(name);
			if (e != null)
				type = e.canBeInstanced ? e.type : null;
		}
		else {
			return null; // Found in inheritance -> cannot be instanced
		}

		return type;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure
	* ScopeTypeEnum scopeType: class, func, if, while global
	* In case ScopeTypeEnum is class or func- t is the type of them */
	/***************************************************************************/
	public void beginScope(ScopeTypeEnum scopeType, TYPE t)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		TYPE_FOR_SCOPE_BOUNDARIES new_scope = new TYPE_FOR_SCOPE_BOUNDARIES("", scopeType, t, this.current_scope_boundary);
		enter("SCOPE-BOUNDARY", new_scope, false);
		this.current_scope_boundary = new_scope;

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		this.current_scope_boundary = this.current_scope_boundary.prev_scope;
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
	}

	public ScopeTypeEnum getCurrentScopeType()
	{
		return this.current_scope_boundary.scope_type_enum;
	}

	public TYPE_FOR_SCOPE_BOUNDARIES getCurrentScopeBoundary()
	{
		return this.current_scope_boundary;
	}

	/* Receives: name
	   Returns the TYPE in entry named "name" by searching only in current scope*/
	public TYPE findInLastScope(String name)
	{
		SYMBOL_TABLE_ENTRY e;

		for (e = top; e != null; e = e.prevtop)
		{
			if (e.type instanceof TYPE_FOR_SCOPE_BOUNDARIES)
			{
				break;
			}
			if (e.name.equals(name))
			{
				return e.type;
			}
		}
		return null;
	}

	/* Receives: name
	   Returns the TYPE of entry named "name" in the global scope*/
	public SYMBOL_TABLE_ENTRY findEntryInGlobal(String name)
	{
		SYMBOL_TABLE_ENTRY e;
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (e.scopeEnum == ScopeTypeEnum.GLOBAL && name.equals(e.name))
			{
				return e;
			}
		}

		return null;
	}

	/* Receives: name
	   Returns the TYPE of entry named "name" in the global scope*/
	private SYMBOL_TABLE_ENTRY findInGlobal(String name)
	{
		SYMBOL_TABLE_ENTRY e = findEntryInGlobal(name);
		if (e == null) return null;
		return e;
	}

	public boolean typeCanBeInstanced(String name)
	{
		SYMBOL_TABLE_ENTRY e = findEntryInGlobal(name);
		if (e == null) return false;
		return e.canBeInstanced;
	}

	public TYPE getLastFunc()
	{
		SYMBOL_TABLE_ENTRY e;

		for (e = top; e != null; e = e.prevtop)
		{
			if (e.type instanceof TYPE_FOR_SCOPE_BOUNDARIES)
			{
				if (((TYPE_FOR_SCOPE_BOUNDARIES) e.type).scope_type_enum == ScopeTypeEnum.FUNC)
				{
					return ((TYPE_FOR_SCOPE_BOUNDARIES) e.type).class_func_type;
				}
			}
		}
		return null;
	}

	/* Receives: name
	   Returns the TYPE of the data member named "name" by searching in current open class and fathers
	   If we are not in a class OR there is no data member with this name- returns null*/
	private TYPE findInInheritance(String name)
	{
		TYPE_CLASS current_class= getCurrentClass();
		return findInInheritance(name, current_class);
	}

	/*  Receives: name and current_class
   		Returns the TYPE of the data member named "name" by searching in current_class and fathers
   		If there is no data member with this name- returns null*/
	public TYPE findInInheritance(String name, TYPE_CLASS current_class) {
		while (current_class != null)
		{
			TYPE_LIST list_pointer = current_class.data_members;
			while (list_pointer != null)
			{
				if (list_pointer.head.name.equals(name))
				{
					return list_pointer.head;
				}
				list_pointer = list_pointer.tail;
			}
			current_class = current_class.father;
		}
		return null;
	}
	/*  Returns the TYPE_CLASS current open class.
	* 	If there is no class open- returns null*/
	public TYPE_CLASS getCurrentClass()
	{
		if (this.getCurrentScopeType() == ScopeTypeEnum.GLOBAL) return null;

		/* If, While, function ot class scope*/
		SYMBOL_TABLE_ENTRY e;

		for (e = top; e != null; e = e.prevtop)
		{
			if (e.type instanceof TYPE_FOR_SCOPE_BOUNDARIES)
			{
				if (((TYPE_FOR_SCOPE_BOUNDARIES) e.type).scope_type_enum == ScopeTypeEnum.CLASS)
				{
					return (TYPE_CLASS)((TYPE_FOR_SCOPE_BOUNDARIES) e.type).class_func_type;
				}
			}
		}
		return null;
	}

	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			instance.beginScope(ScopeTypeEnum.GLOBAL, null);

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TYPE_INT.getInstance(), true);
			instance.enter("string",TYPE_STRING.getInstance(), true);

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/
			instance.enter("void",TYPE_STRING.getInstance(), false);

			/***************************************/
			/* [3] Enter library function PrintInt, PrintString */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						new TYPE_VAR("intForPrint", TYPE_INT.getInstance()),
						null)),false);
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintString",
							new TYPE_LIST(
									new TYPE_VAR("stringForPrint", TYPE_STRING.getInstance()),
									null)), false);
			
		}
		return instance;
	}
}
