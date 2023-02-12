package src.IR;
import src.IR.*;


public class FunctionIRList {
    public IR.IRcommandList start;
    public int len;
    public FunctionIRList next;

    public FunctionIRList(IR.IRcommandList start, int len){
        this.start = start;
        this.len = len;
    }

    public void AnalyzeMe() {
        CFG_Node[] CFG = this.Create_CFG();
    }

    public CFG_Node[] Create_CFG(){
        CFG_Node[] arr = new CFG_Node[len];
        IR.IRcommandList curr = this.start;

        //first of all each one should point to the next cmd except for when next cmd in under a label
        for(int i = 0; i < this.len; i++){
            if((curr.head != null) && !(curr.head instanceof IR.IRcommand_Label)){//wtf does it want here? why error?
                arr[i] = new CFG_Node(i, curr.head);
            }
            curr = curr.tail;
        }

        //now traverse all over, find and handle Jumps and conditionals
        curr = this.start;
        for(int i = 0; i < this.len; i++){
            //if we finished traversing or we are on a label statement no need to treat this cmd, continue
            if(curr.head == null || curr.head instanceof IR.IRcommand_Label){//should we really test curr.head == null? when will we have an empty IRCommand?
                curr = curr.tail;
                continue;
            }

            //if it's a not a jump then the current node points to the next
            if(!(curr.head instanceof IR.IRcommand_Jump_Label)){
                if(i < this.len - 1){
                    arr[i].sons.add(arr[i + 1]);
                }
            }

            //if it is some kind of jump label we would look for the label within the function. if the label is not here then we don't have to do anything.
            if(curr.head instanceof IR.IRcommand_Jump_Label || curr.head instanceof IR.IRcommand_Jump_If_Eq_Zero){
                boolean has_found_current_label_in_function = false;
                for(int j = 0; j < this.len; j++){

                    //if we found a label check if the current label is the label referenced by the jump cmd at arr[i]
                    if(arr[j].cmd instanceof IR.IRcommand_Label){
                        if((arr[i].cmd instanceof IR.IRcommand_Jump_Label &&  ((IR.IRcommand_Jump_Label) arr[i].cmd).label_name.equals(((IR.IRcommand_Label)arr[j].cmd).label_name)) ||
                            (arr[i].cmd instanceof IR.IRcommand_Jump_If_Eq_To_Zero &&  ((IR.IRcommand_Jump_If_Eq_To_Zero) arr[i].cmd).label_name.equals(((IR.IRcommand_Label)arr[j].cmd).label_name))){
                            has_found_current_label_in_function = true;
                        }
                    }

                    //if we found the label then arr[j] is the label command that arr[i] can jump to. we want to link arr[i[ to the command AFTER the label
                    if(has_found_current_label_in_function && arr[j].cmd instanceof IR.IRcommand_Label){
                        arr[i].sons.add(arr[j + 1]);
                    }
                }


            }
        }

        return arr;
    }
}