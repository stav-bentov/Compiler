package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	/* made on TYPE_VAR's type
	   check if expType can be assign to (this) TYPE_VAR's type
	 */
	public boolean checkAssign(TYPE expType)
	{
		if (expType instanceof TYPE_NIL)
		{
			/* Only variables of arrays and classes can be defined with null expression */
			if (this instanceof TYPE_CLASS || this instanceof TYPE_ARRAY)
				return true;
		}
		else
		{
			if (this instanceof TYPE_CLASS && expType instanceof TYPE_CLASS)
			{
				if (((TYPE_CLASS) expType).inheritsFrom((TYPE_CLASS)this))
					return true;
			}
			else
			{
				/* Cases of int/ string/ array*/
				if(this == expType)
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean isVar(){ return false;}
}
