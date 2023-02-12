package src.IR;

public class Analysis {
    public static FunctionIRList init(){
        IR.IRcommandList start = null;
        IR.IRcommandList curr = IR.getInstance().tail;

        FunctionIRList ret = new FunctionIRList(null, null, 0);
        FunctionIRList temp = ret;

        int len = 1;
        while(curr != null){
            //if we go into this if clause we know we found a label that starts a function
            if((curr.head != null) && (curr.head instanceof IR.IRcommand_Label) && (IR.IRcommand_Label curr.head).label_type == labelEnum.FUNCSTART){//wtf does it want here? why error?
                start = curr.tail;
                len = 1;
            }

            //here we see a label that marks the end of a function
            if((curr.head != null) && (curr.head instanceof IR.IRcommand_Label) && (IR.IRcommand_Label curr.head).label_type == labelEnum.FUNCEND){//wtf does it want here? why error?
                //if we also saw a start to that function we create a function ir list object and move to the next one
                if(start != null){
                    temp.next = new FunctionIRList(start, len);
                    start = null;
                    len = 0;
                    temp = temp.next;
                }
            }

            curr = curr.tail;
            len++;
        }
    }

    public static void AnalyzeMe(){
        FunctionIRList function_IRcommands_list = init();
        while(function_IRcommands_list != null){
            function_IRcommands_list.AnalyzeMe();
        }
    }
}
