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
	 * a is an argument's list (TYPE_VAR) of a function, b is an argument's list (TYPE_VAR) of a function, check if they have the same types*/
	public boolean equalsForComparingSignature(Object o)
	{
		/* Designate for comparing parameters list */
		if (!(o instanceof TYPE_LIST))
			return false;

		TYPE_LIST compared_pointer = (TYPE_LIST) o;
		TYPE_LIST this_pointer = this;

		while (this_pointer.head != null && compared_pointer.head != null)
		{
			TYPE currHeadType = ((TYPE_VAR) this_pointer.head).type;
			TYPE comperedHeadType = ((TYPE_VAR) compared_pointer.head).type;
			if(currHeadType != comperedHeadType)
				return false;
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
	* If A is an argument's list (TYPE_VAR) , B is type's list (TYPE) (given from expsList), A.compareGivenParam(B) will return true if there is a match*/
	public boolean equalsForValidatingGivenParams(Object o)
	{
		/* Designate for comparing parameters list */
		if (!(o instanceof TYPE_LIST))
			return false;

		TYPE_LIST variablesPointer = (TYPE_LIST) o;
		TYPE_LIST paramsPointer = this;

		while (paramsPointer.head != null && variablesPointer.head != null) {
			TYPE currType = ((TYPE_VAR)paramsPointer.head).type;
			if (currType instanceof TYPE_CLASS) {
				/* class can be null but if not- check if it's same class or inherited*/
				if (!(paramsPointer.head instanceof TYPE_NIL)) {
					if (currType != variablesPointer.head) {
						if (!((TYPE_CLASS)variablesPointer.head).inheritsFrom(currType)) return false;
					}
				}
			}
			else if (currType instanceof TYPE_ARRAY) {
				/* class array be null but if not- check if it's same array*/
				if (!(variablesPointer.head instanceof TYPE_NIL)) {
					if (paramsPointer.head != variablesPointer.head) return false;
				}
			}
			else {
				if(paramsPointer != variablesPointer.head)
					return false;
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
