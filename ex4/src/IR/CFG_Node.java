package src.IR;

import java.util.ArrayList;
import java.util.List;

public class CFG_Node {
    //node_num is used for the liveness analysis. there we do Out(i) = Union the Ins of all his sons.
    // when we reach a son we want to know his index. the node_num is his index.
    int node_num;
    IR.IRcommand cmd;
    List<CFG_Node> sons = new ArrayList<CFG_Node>();
    public CFG_Node(int node_num, IR.IRcommand cmd){
        this.node_num = node_num;
        this.cmd = cmd;
    }

}
