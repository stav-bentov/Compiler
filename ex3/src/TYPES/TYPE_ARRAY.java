package TYPES;

public class TYPE_ARRAY extends TYPE{

    /* The type can be a class/array/primitive type*/
    public TYPE arrayType;
    public int arraySize;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(TYPE arrayType,String name)
    {
        this.name = name;
        this.arrayType = arrayType;
    }

    @Override
    public boolean isArray(){ return true;}

    @Override
    /* TODO: I think it might be redundant
       Receives Object
       If it's not from TYPE_ARRAY- returns false
       else- Checks the type *types* (will return true for different arrays but from same types*/
    public boolean equals(Object o)
    {
        if (!(o instanceof TYPE_ARRAY)) return false;
        TYPE_ARRAY comperedArray = (TYPE_ARRAY) o;

        /* Compare types and if type is an Array or Class-> compare them! */
        if (!(this.arrayType.getClass().equals(comperedArray.arrayType.getClass()))) return false;
        if (this.arrayType instanceof TYPE_ARRAY || this.arrayType instanceof TYPE_CLASS)
        {
            return (this.arrayType.equals(comperedArray.arrayType));
        }
        return true;
    }
}
