package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Class_Method extends IRcommand_Call_Func {
    TEMP classPtr;
    int methodOffset;

    public IRcommand_Call_Class_Method(TEMP_LIST param_list, TEMP assigned_temp, int methodOffset, TEMP classPtr) {
        this(param_list, assigned_temp, methodOffset);
        this.classPtr = classPtr;
    }

    public IRcommand_Call_Class_Method(TEMP_LIST param_list, int methodOffset, TEMP classPtr) {
        this(param_list, methodOffset);
        this.classPtr = classPtr;
    }

    public IRcommand_Call_Class_Method(TEMP_LIST param_list, TEMP assigned_temp, int methodOffset) {
        super(param_list, assigned_temp);
        this.methodOffset = methodOffset;
    }

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
