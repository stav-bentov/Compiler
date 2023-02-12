package src.IR;

public class Analysis {
    public static FunctionIRList init(){
        IR.IRcommandList start = null;
        IR.IRcommandList curr = IR.getInstance().head;//TODO: check how to get first command of all IR commands file

        FunctionIRList ret = new FunctionIRList(null, 0);
        FunctionIRList temp = ret;

        int len = 1;
        while(curr != null){
            //if we go into this if clause we know we found a label that starts a function
            if((curr.head instanceof IR.IRcommand_Label) && ((IR.IRcommand_Label curr.head).label_type == labelEnum.FUNCSTART)){//IRCommand instance of IRCommandStartFunc
                start = curr;
                len = 1;
            }

            //here we see a label that marks the end of a function
            if((curr.head instanceof IR.IRcommand_Label) && (IR.IRcommand_Label curr.head).label_type == labelEnum.FUNCEND){
                //if we also saw a start to that function we create a function ir list object and move to the next one
                temp = new FunctionIRList(start, len);
                start = null;
                len = 0;
                temp = temp.next;
            }

            curr = curr.tail;
            len++;
        }

        return ret;
    }

    public static void AnalyzeMe() throws Exception{
        FunctionIRList function_IRcommands_list = init();
        while(function_IRcommands_list != null){
            function_IRcommands_list.AnalyzeMe();
        }
    }
}
