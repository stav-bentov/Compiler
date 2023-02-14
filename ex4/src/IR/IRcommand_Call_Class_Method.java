package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Class_Method extends IRcommand_Call_Func {
    TEMP classPtr;
    int methodOffset;

    /* With return, with var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, TEMP assigned_temp, int methodOffset, TEMP classPtr) {
        this(param_list, assigned_temp, methodOffset);
        this.classPtr = classPtr;

        this.dest = assigned_temp;
        depends_on.add(classPtr);
    }

    /* No return, with var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, int methodOffset, TEMP classPtr) {
        this(param_list, methodOffset);
        this.classPtr = classPtr;

        depends_on.add(classPtr);
    }

    /* With return, no var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, TEMP assigned_temp, int methodOffset) {
        super(param_list, assigned_temp);
        this.methodOffset = methodOffset;
    }

    /* No return, no var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, int methodOffset) {
        super(param_list);
        this.methodOffset = methodOffset;
    }

    @Override
    public void call() {
        /* When class_ptr is null, will use "this" instead */
        MIPSGenerator.getInstance().call_class_method(methodOffset, classPtr);
    }
}
