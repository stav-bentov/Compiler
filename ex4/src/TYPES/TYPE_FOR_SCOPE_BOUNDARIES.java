package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	/****************/
	/* CTROR(S) ... */
	/****************/
	public ScopeTypeEnum scope_type_enum;
	public TYPE class_func_type; // TYPE_CLASS or TYPE_FUNCTION that represents the current scope boundary
	public TYPE_FOR_SCOPE_BOUNDARIES prev_scope;

	public TYPE_FOR_SCOPE_BOUNDARIES(String name, ScopeTypeEnum scope_type_enum, TYPE class_func_type, TYPE_FOR_SCOPE_BOUNDARIES prev_scope)
	{
		this.name = name;
		this.scope_type_enum = scope_type_enum;
		this.class_func_type = class_func_type;
		this.prev_scope = prev_scope;
	}
}
