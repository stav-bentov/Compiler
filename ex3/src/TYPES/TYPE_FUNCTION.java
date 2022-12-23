package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof TYPE_FUNCTION)) return false;

		TYPE_FUNCTION compered_func = (TYPE_FUNCTION) o;
		/* Compare: 1. names,
					2. return types
					3. parameter's types */
		if (!(this.name.equals(compered_func.name))) return false;
		if (!(this.CompareReturnType(compered_func))) return false;
		if (!(this.params.equals(compered_func.params))) return false;
		return true;
	}

	private boolean CompareReturnType(TYPE_FUNCTION compered_func)
	{
		if (this.returnType.getClass().equals(compered_func.returnType.getClass()))
		{
			/* Same inst_type of return- need to check extra in case of array (array of arrays or array of classes) or class*/
			if (this.returnType instanceof TYPE_ARRAY)
			{
				if (!(this.returnType.equals(compered_func.returnType))) return false;
			}

			if (this.returnType instanceof TYPE_CLASS)
			{
				if (!(this.returnType.equals(compered_func.returnType))) return false;
			}
			return true;
		}
		return false;
	}
}
