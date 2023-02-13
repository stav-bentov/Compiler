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

        this.dest = assigned_temp.getSerialNumber();
        while(param_list != null){
            this.depends_on.add(param_list.head.getSerialNumber());
            param_list = param_list.tail;
        }
        depends_on.add(classPtr.getSerialNumber());
    }

    /* No return, with var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, int methodOffset, TEMP classPtr) {
        this(param_list, methodOffset);
        this.classPtr = classPtr;

        while(param_list != null){
            this.depends_on.add(param_list.head.getSerialNumber());
            param_list = param_list.tail;
        }
        depends_on.add(classPtr.getSerialNumber());
    }

    /* With return, no var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, TEMP assigned_temp, int methodOffset) {
        super(param_list, assigned_temp);
        this.methodOffset = methodOffset;

        this.dest = assigned_temp.getSerialNumber();
        while(param_list != null){
            this.depends_on.add(param_list.head.getSerialNumber());
            param_list = param_list.tail;
        }
    }

    /* No return, no var */
    public IRcommand_Call_Class_Method(TEMP_LIST param_list, int methodOffset) {
        super(param_list);
        this.methodOffset = methodOffset;

        while(param_list != null){
            this.depends_on.add(param_list.head.getSerialNumber());
            param_list = param_list.tail;
        }
    }

    @Override
    public void call() {
        /* When class_ptr is null, will use "this" instead */
        MIPSGenerator.getInstance().call_class_method(methodOffset, classPtr);
    }
}
