package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head, TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}

	public void Merge(TYPE_LIST other){
		TYPE_LIST curr = this;
		while(curr.tail != null){
			curr = curr.tail;
		}

		curr.tail = other;
	}
}
