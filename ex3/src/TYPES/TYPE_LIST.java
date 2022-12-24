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

	/* Compare *types* of 2 lists (for checking same signature)
	 * a is an argument's list of a function, b is an argument's list of a function, check if they have the same types*/
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
			if (this_pointer.head instanceof TYPE_ARRAY || this_pointer.head instanceof TYPE_CLASS)
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

	/* Validate that the parameters given to function a is validate according to it's parameters types
	* a is an argument's list, b is type list (given from expsList), a.compareGivenParam(b) will return true if there is a match*/
	public boolean validateGivenParam(Object o)
	{
		/* Designate for comparing parameters list */
		if (!(o instanceof TYPE_LIST)) return false;

		TYPE_LIST variablesPointer = (TYPE_LIST) o;
		TYPE_LIST paramsPointer = this;

		while (paramsPointer.head != null && variablesPointer.head != null) {
			if (paramsPointer.head instanceof TYPE_CLASS) {
				/* class can be null but if not- check if it's same class or inherited*/
				if (!(variablesPointer.head instanceof TYPE_NIL)) {
					if(!(paramsPointer.head.getClass().equals(variablesPointer.head.getClass()))) return false;
					if (!(paramsPointer.head.equals(variablesPointer.head))) {
						if (!(TYPE_CLASS)variablesPointer.inheritsFrom(paramsPointer)) return false;
					}
				}
			}
			else if (paramsPointer.head instanceof TYPE_ARRAY) {
				/* class array be null but if not- check if it's same array*/
				if (!(variablesPointer.head instanceof TYPE_NIL)) {
					if (!(paramsPointer.head.getClass().equals(variablesPointer.head.getClass()))) return false;
					if (!(paramsPointer.head.equals(variablesPointer.head))) return false;
				}
			}
			else {
				if(paramsPointer.head.getClass().equals(variablesPointer.head.getClass())) return false;
			}

			variablesPointer = variablesPointer.tail;
			paramsPointer = paramsPointer.tail;
		}

		/* Different lengths */
		if (paramsPointer.head != null || variablesPointer.head != null){
			return false;
		}
		return true;
	}
}
