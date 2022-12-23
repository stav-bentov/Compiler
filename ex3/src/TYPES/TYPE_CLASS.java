package TYPES;

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
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	/********************************************************/
	/* a.inheritsFrom(b) returns true iff a inherits from b */
	/********************************************************/
	public boolean inheritsFrom(TYPE_CLASS c) {
		TYPE_CLASS currFather = this.father;

		while (currFather != null) {
			if (currFather == c) {
				return true;
			}
			currFather = currFather.father;
		}

		return false;
	}
}
