package hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue; 


class Astar{

    public static void astar(Node root){
        // Comparator class to compare the heuristic value 
        // while adding the node in PriorityQueue 
        class NodeComparator implements Comparator<Node> { 
            public int compare(Node n1, Node n2) 
            { 
                if (n1.total_heuristics < n2.total_heuristics) 
                    return -1; 
                if (n1.total_heuristics > n2.total_heuristics) 
                    return 1; 
                return 0; 
            } 
        } 
    

        // PriorityQueue to store the nodes 
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new NodeComparator());
        Comparator<Node> heuristicComparator = Comparator.comparing(node -> node.robot_coordinates, Node::compareTo).thenComparingDouble(node -> node.total_heuristics);
        TreeSet<Node> visitedNodes = new TreeSet<>(heuristicComparator);
        Set<Node> solutions = new HashSet<>();
        pq.add(root);

        while(pq.size() > 0) {
            Node currNode = pq.poll();
            if(!currNode.isSolved()){
                currNode.generate_children();
                for(Node child : currNode.children){
                    if (child != null)
                        if(visitedNodes.ceiling(child) != null)
                         if(child.total_heuristics < visitedNodes.ceiling(child).total_heuristics)
                            pq.add(child);
                }
            }
            else{
                solutions.add(currNode);
            }
            visitedNodes.add(currNode);
        }
        System.out.println(solutions.toString());
    }




    public static void main(String[] args){
        // Declare variables for the table size
        int rows;
        int cols;
        
        // Declare a 2D array to store the table
        int[][] table = new int[1][1];
        
        // Declare variables for start and finish coordinates
        int startY= 0;
        int startX= 0;
        int goalY= 0;
        int goalX= 0;
        
        // Open the file and read it using Scanner
        File file = new File("path.txt");
        try {
            Scanner scanner = new Scanner(file);
            
            // Read the first line and set the table size
            String[] size = scanner.nextLine().split(" ");
            rows = Integer.parseInt(size[0]);
            cols = Integer.parseInt(size[1]);
            
            // Initialize the table
            table = new int[rows][cols];
            
            // Read each line and set the table values
            for (int row = 0; row < rows; row++) {
            String line = scanner.nextLine();
            for (int col = 0; col < cols; col++) {
                table[row][col] = line.charAt(col) - '0';
            }
            }
            
            // Read the last 2 lines and set start and finish coordinates
            String[] start = scanner.nextLine().split(" ");
            startY = Integer.parseInt(start[0]);
            startX = Integer.parseInt(start[1]);
            
            String[] finish = scanner.nextLine().split(" ");
            goalY = Integer.parseInt(finish[0]);
            goalX = Integer.parseInt(finish[1]);
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        
        Grid grid = new Grid(table, new int[]{startY, startX}, new int[]{goalY, goalX});
        Node root = new Node(grid);

        
    }
    
}