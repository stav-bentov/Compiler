package IR;

import java.util.ArrayList;
import java.util.List;

public class Analysis {
    private static List<FunctionIRList> function_list;

    public static void init(){
        function_list = new ArrayList<>();
        IRcommandList start = null;
        IRcommandList curr = IR.getInstance().tail;
        IRcommand firstCmd = IR.getInstance().head;

        int len = 0;
        if((firstCmd instanceof IRcommand_Start_Func)){//IRCommand instance of IRCommandStartFunc
            start = curr;
            len = 1;
        }

        while(curr != null){
            //if we go into this if clause we know we found a label that starts a function
            if(curr.head instanceof IRcommand_Start_Func){//IRCommand instance of IRCommandStartFunc
                start = curr;
                len = 1;//maybe should be 0?
            }

            //here we see a label that marks the end of a function
            if(curr.head instanceof IRcommand_End_Func){
                //if we also saw a start to that function we create a function ir list object and move to the next one
                function_list.add(new FunctionIRList(start, len + 1)); //adding one to include IRCommand_End_Func

                start = null;
                len = 0;
            }

            curr = curr.tail;
            len++;
        }
    }

    public static void AnalyzeMe() throws Exception{
        init();
        for(FunctionIRList function : function_list){
            function.AnalyzeMe();
        }
    }
}
