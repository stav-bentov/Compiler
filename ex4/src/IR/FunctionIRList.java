package IR;

import TEMP.TEMP;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FunctionIRList {
    public IRcommandList start;
    public int len;

    //private fields for chaitin's algorithm
    private CFG_Node[] CFG;
    private List<Set<Integer>> In;
    private List<Set<Integer>> Out;
    private Map<Integer, Set<Integer>> graph;
    private Map<Integer, Set<Integer>> copy_of_graph;
    private Stack<Integer> stack;
    private HashMap<Integer, Integer> coloring;
    private final int num_of_registers = 10;

    public FunctionIRList(IRcommandList start, int len){
        this.start = start;
        this.len = len;
    }

    public void AnalyzeMe() throws Exception{
        Create_CFG();
        AnalyzeLiveness();
        BuildInterferenceGraph();
        ColorGraph();
        AssignTemps();
    }

    private void Create_CFG(){
        CFG_Node[] arr = new CFG_Node[len];
        IRcommandList curr = start;

        //init all nodes with their relevant command
        for(int i = 0; i < len; i++){
            //what if there is a return in the middle of a function?
            //should CFG_node be created for a label cmd?
            if(curr.head != null){//removed: !(curr.head instanceof IRcommand_Label)
                arr[i] = new CFG_Node(i, curr.head);
            }
            curr = curr.tail;
        }

        //now traverse all over, find and handle Jumps and conditionals
        curr = start;
        for(int i = 0; i < len; i++){
            //if we finished traversing or we are on a label statement no need to treat this cmd, continue
            if(curr.head == null || curr.head instanceof IRcommand_Label){//should we really test curr.head == null? when will we have an empty IRCommand?
                curr = curr.tail;
                continue;
            }

            //if it's a not a jump then the current node points to the next
            if(!(curr.head instanceof IRcommand_Jump_Label)){
                if(i < len - 1){
                    arr[i].sons.add(arr[i + 1]);
                }
            }

            //if it is some kind of jump label we would look for the label within the function. if the label is not here then we don't have to do anything.
            if(curr.head instanceof IRcommand_Jump_Label || curr.head instanceof IRcommand_Jump_If_Eq_To_Zero){
                for(int j = 0; j < len; j++){//think whether to change implementation: save all labels in hash-table so we don't need to iterate over all commands

                    //if we found a label check if the current label is the label referenced by the jump cmd at arr[i]
                    if(arr[j].cmd instanceof IRcommand_Label){//TODO: fix ifs to be more readable
                        if((arr[i].cmd instanceof IRcommand_Jump_Label &&  ((IRcommand_Jump_Label) arr[i].cmd).label_name.equals(((IRcommand_Label)arr[j].cmd).label_name)) ||
                            (arr[i].cmd instanceof IRcommand_Jump_If_Eq_To_Zero &&  ((IRcommand_Jump_If_Eq_To_Zero) arr[i].cmd).after_if_body_label.equals(((IRcommand_Label)arr[j].cmd).label_name))){
                            arr[i].sons.add(arr[j]);
                            break;
                        }
                    }
                }
            }
            curr = curr.tail;
        }

        CFG = arr;
    }

    private void AnalyzeLiveness(){
        Set<Integer> old_out;
        Set<Integer> old_in;
        InitInAndOut();

        boolean has_changed = true;
        //we run the liveness again and again until nothing has changed
        while(has_changed){
            has_changed = false;
            //we compute liveness bottom up
            for(int i = len - 1; i >= 0; i--){

                //update the Out of Node i
                old_out = Out.get(i);
                for (CFG_Node son : CFG[i].sons){
                    Out.get(i).addAll(In.get(son.node_num));
                }

                //if something has changed we need to run again
                has_changed = !(old_out.equals(Out.get(i)));

                //update the In of Node i
                old_in = In.get(i);
                In.get(i).addAll(Out.get(i));
                System.out.println("CFG[i].cmd "+ CFG[i].cmd);
                System.out.println("CFG[i].cmd.depends_on "+ CFG[i].cmd.depends_on);
                Set<Integer> depends_on = CFG[i].cmd.depends_on.stream().map(temp -> temp.getSerialNumber()).collect(Collectors.toSet());
                System.out.println("Depends on = "+ depends_on);
                In.get(i).addAll(depends_on);//Depends on are the temps which are required for this calculations (a,b in case of y = a + b)
                if(CFG[i].cmd.dest != null){//destination is the temp number in which we put the result of the calculation (y in case of y = a + b)
                    In.get(i).remove(CFG[i].cmd.dest.getSerialNumber());
                }

                //if something has changed we need to run again
                if(!has_changed){
                    has_changed = !(old_in.equals(In.get(i)));
                }
            }
        }
    }

    private void InitInAndOut(){
        In = new ArrayList<>();
        Out = new ArrayList<>();
        for(int i = 0 ; i < len; i++){
            In.add(new HashSet<>());
            Out.add(new HashSet<>());
        }
    }

    private void BuildInterferenceGraph(){
        graph = new HashMap<Integer, Set<Integer>>();//key is node value is neighbours of the node
        copy_of_graph = new HashMap<Integer, Set<Integer>>();//key is node value is neighbours of the node

        //get all nodes for the graph
        Set<Integer> nodes = new HashSet<>();
        for(int i = 0; i < len; i++){
            nodes.addAll(Out.get(i));
        }

        //insert vertices
        for(Integer node : nodes){
            graph.put(node, new HashSet<>());
            copy_of_graph.put(node, new HashSet<>());
        }

        //insert edges
        for(Set<Integer> liveness_set : Out){
            //if set is of size 1 no need to add edge - no self edges in graph
            if(liveness_set.size() <= 1) continue;

            //add all edges between all nodes in liveness_set
            for(Integer i: liveness_set){
                for(Integer j: liveness_set){
                    if(!i.equals(j)){
                        graph.get(i).add(j);
                        copy_of_graph.get(i).add(j);

                        graph.get(j).add(i);
                        copy_of_graph.get(j).add(i);
                    }
                }
            }
        }
    }

    private void ColorGraph() throws Exception{
        stack = new Stack<Integer>();

        //remove one by one all nodes with degree lower than k (simplify)
        Integer nodeToRemove = GetNodeWithDegreeSmallerThanNumOfRegs();
        while(nodeToRemove != null){
            RemoveNodeFromGraph(nodeToRemove);
            stack.push(nodeToRemove);

            nodeToRemove = GetNodeWithDegreeSmallerThanNumOfRegs();
        }

        //according to heuristic in class, if cannot remove and graph is not empty it's not k-colorable
        //ask if forum if this is ok
        if(!graph.isEmpty()){
            throw new Exception("Couldn't convert to 10 temporaries");
        }

        //init the coloring
        coloring = new HashMap<Integer, Integer>();//key is node value is color
        for(Integer node : stack){
            coloring.put(node, -1);
        }

        //set the colors of the of the nodes (output saves in this.coloring)
        Integer node;
        while(!stack.isEmpty()){
            node = stack.pop();
            AssignColor(node);
        }
    }

    private Integer GetNodeWithDegreeSmallerThanNumOfRegs(){
        for(Integer node : graph.keySet()){
            if(graph.get(node).size() < num_of_registers){
                return node;
            }
        }

        return null;
    }

    private void RemoveNodeFromGraph(Integer nodeToRemove){
        //remove node from his neighbour's neighbour list
        for(Integer neighbour : graph.get(nodeToRemove)){
            graph.get(neighbour).remove(nodeToRemove);
        }

        //remove the node itself
        graph.remove(nodeToRemove);
    }

    //according to the heuristic studied in class assigns the lowest color available
    private void AssignColor(Integer node) throws Exception{
        boolean has_neighbour_with_this_color;
        Set<Integer> neighbours = copy_of_graph.get(node);
        for(int color = 0; color < num_of_registers; color++){
            has_neighbour_with_this_color = false;
            for(Integer neighbour : neighbours){
                if(coloring.get(neighbour) == color){
                    has_neighbour_with_this_color = true;
                }
            }

            if(!has_neighbour_with_this_color){
                coloring.replace(node, color);
                return;//thank you stav
            }
        }

        throw new Exception("graph is not colorable with the heuristic studied in class!");
    }

    private void AssignTemps(){
        for(int i = 0; i < len; i++){
            IRcommand cmd = CFG[i].cmd;
            //IRCommand needs to have a field temps - all the temps in the IRCommand x = a + b (x,a,b)
            for(TEMP temp : cmd.depends_on){
                temp.SetRegisterSerialNumber(coloring.getOrDefault(temp.getSerialNumber(), 0));
            }
        }
    }
}