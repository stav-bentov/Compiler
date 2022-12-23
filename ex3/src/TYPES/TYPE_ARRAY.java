package TYPES;

public class TYPE_ARRAY extends TYPE{

    /* The type can be a class/array/primitive type*/
    public TYPE array_type;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(TYPE array_type,String name)
    {
        this.name = name;
        this.array_type = array_type;
    }

    @Override
    public boolean isArray(){ return true;}

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TYPE_ARRAY)) return false;
        TYPE_ARRAY compered_array = (TYPE_ARRAY) o;

        /* Compare types and if type is an Array or Class-> compare them! */
        if (!(this.array_type.getClass().equals(compered_array.array_type.getClass()))) return false;
        if (this.array_type instanceof TYPE_ARRAY || this.array_type instanceof TYPE_CLASS)
        {
            return (this.array_type.equals(compered_array.array_type));
        }
        return true;
    }
}
