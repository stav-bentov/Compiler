package IR;

import java.util.ArrayList;
import java.util.List;

public class IR_Node {
    //node_num is used for the liveness analysis. there we do Out(i) = Union the Ins of all his sons.
    // when we reach a son we want to know his index. the node_num is his index.
    int node_num;
    IRcommand cmd;
    List<IR_Node> sons = new ArrayList<>();
    public IR_Node(int node_num, IRcommand cmd){
        this.node_num = node_num;
        this.cmd = cmd;
    }

}
