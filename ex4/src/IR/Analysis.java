package IR;

import IR.*;

public class Analysis {
    public static FunctionIRList init(){
        IRcommandList start = null;
        IRcommandList curr = IR.getInstance().tail;
        IRcommand firstCmd = IR.getInstance().head;

        FunctionIRList ret = new FunctionIRList(null, 0);
        FunctionIRList temp = ret;

        int len = 0;
        if((firstCmd instanceof IRcommand_Start_Func)){//IRCommand instance of IRCommandStartFunc
            start = curr;
            len = 1;
        }

        while(curr != null){
            //if we go into this if clause we know we found a label that starts a function
            if(curr.head instanceof IRcommand_Start_Func){//IRCommand instance of IRCommandStartFunc
                start = curr;
                len = 1;
            }

            //here we see a label that marks the end of a function
            if(curr.head instanceof IRcommand_End_Func){
                //if we also saw a start to that function we create a function ir list object and move to the next one
                temp.len = len;
                temp.start = start;
                temp.next = new FunctionIRList(null, 0);
                temp = temp.next;

                start = null;
                len = 0;
            }

            curr = curr.tail;
            len++;
        }

        temp.next = null;
        return ret;
    }

    public static void AnalyzeMe() throws Exception{
        FunctionIRList function_IRcommands_list = init();
        while(function_IRcommands_list != null){
            function_IRcommands_list.AnalyzeMe();
            function_IRcommands_list = function_IRcommands_list.next;
        }
    }
}
