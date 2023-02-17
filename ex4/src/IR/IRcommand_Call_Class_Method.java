package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.List;

public class IRcommand_Call_Class_Method extends IRcommand_Call_Func {
    TEMP classPtr;
    int methodOffset;

    /* With return, with var */
    public IRcommand_Call_Class_Method(List<TEMP> param_list, TEMP assigned_temp, int methodOffset, TEMP classPtr) {
        this(param_list, assigned_temp, methodOffset);
        this.classPtr = classPtr;

        depends_on.add(classPtr);
    }

    /* No return, with var */
    public IRcommand_Call_Class_Method(List<TEMP> param_list, int methodOffset, TEMP classPtr) {
        this(param_list, methodOffset);
        this.classPtr = classPtr;

        depends_on.add(classPtr);
    }

    /* With return, no var */
    public IRcommand_Call_Class_Method(List<TEMP> param_list, TEMP assigned_temp, int methodOffset) {
        super(param_list, assigned_temp);
        this.methodOffset = methodOffset;
    }

    /* No return, no var */
    public IRcommand_Call_Class_Method(List<TEMP> param_list, int methodOffset) {
        super(param_list);
        this.methodOffset = methodOffset;
    }

    @Override
    public void call() {
        /* When class_ptr is null, will use "this" instead */
        MIPSGenerator.getInstance().call_class_method(methodOffset);
    }

    @Override
    public void set_arguments() {
        super.set_arguments();
        MIPSGenerator.getInstance().set_class_ptr(this.classPtr);
    }

    @Override
    public void del_arguments(){
        MIPSGenerator.getInstance().del_arguments(1); // Del class ptr (first arg)
        super.del_arguments();
    }
}
