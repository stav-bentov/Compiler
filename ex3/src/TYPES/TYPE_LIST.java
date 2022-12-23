package TYPES;

public class TYPE_LIST extends TYPE
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}

	/* Compare types of 2 lists */
	@Override
	public boolean equals(Object o)
	{
		/* Designate for comparing parameters list */
		if (!(o instanceof TYPE_LIST)) return false;

		TYPE_LIST compared_pointer = (TYPE_LIST) o;
		TYPE_LIST this_pointer = this;

		while (this_pointer.head != null && compared_pointer.head != null)
		{
			if(this_pointer.head.getClass().equals(compared_pointer.head.getClass())) return false;

			/* Same inst_type of nodes- need to check extra in case of an array (array of arrays or array of classes) or class*/
			if (this_pointer.head instanceof TYPE_ARRAY)
			{
				if (!(this_pointer.head.equals(compared_pointer.head))) return false;
			}
			if (this_pointer.head instanceof TYPE_CLASS)
			{
				if (!(this_pointer.head.equals(compared_pointer.head))) return false;
			}

			this_pointer = this_pointer.tail;
			compared_pointer = compared_pointer.tail;
		}

		/* Different lengths */
		if (this_pointer.head != null || compared_pointer.head != null){
			return false;
		}
		return true;
	}
}
