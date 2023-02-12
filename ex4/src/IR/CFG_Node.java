package src.IR;

import java.util.ArrayList;
import java.util.List;

public class CFG_Node {
    int node_num;
    IR.IRcommand cmd;
    List<CFG_Node> sons = new ArrayList<CFG_Node>();
    public CFG_Node(int node_num, IR.IRcommand cmd){
        this.node_num = node_num;
        this.cmd = cmd;
    }

}
