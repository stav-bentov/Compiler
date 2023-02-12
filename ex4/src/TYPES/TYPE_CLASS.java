package TYPES;

import AST.AST_EXP;
import AST.AST_Node;

import java.util.List;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;

	/* Accessible for usage when creating a runtime object when instancing a class */
	public String label_VT;
	public List<AST_Node> field_exps;
	public int numFields;
	public int numMethods;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.label_VT = "vt_" + name;
		this.numFields = 0;
	}

	/********************************************************/
	/* a.inheritsFrom(b) returns true iff a inherits from b (or if a is b) */
	/********************************************************/
	public boolean inheritsFrom(TYPE_CLASS c) {
		TYPE_CLASS curr = this;

		while (curr != null) {
			if (curr == c) {
				return true;
			}
			curr = curr.father;
		}

		return false;
	}
	@Override
	public boolean isClass(){ return true;}

	@Override
	  /* Receives Object o
       If it's not from TYPE_CLASS- returns false
       else- Checks the names*/
	public boolean equals(Object o)
	{
		if (!(o instanceof TYPE_CLASS)) return false;
		if (!(this.name.equals(((TYPE_CLASS)o).name))) return false;
		return true;
	}

}
