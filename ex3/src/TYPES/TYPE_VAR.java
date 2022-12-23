package src.TYPES;

import jdk.nashorn.internal.ir.Symbol;

public class TYPE_VAR extends TYPE{
    public String id;
    public TYPE type;

    public TYPE_VAR(String id){
        this.id = id;
    }

    public boolean isClass(){
        return SYMBOL_TABLE.getInstance().find(id).isClass();
    }

    @Override
    public boolean isVar(){ return true; }
}
