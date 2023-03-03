package hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue; 


class Astar{
	
	public static Node astar_manhattan(Node root){
        // Comparator class to compare the heuristic value 
        // while adding the node in PriorityQueue 
        class NodeComparator implements Comparator<Node> { 
            public int compare(Node n1, Node n2) 
            { 
                // if (n1.total_heuristics < n2.total_heuristics) 
                //     return -1; 
                // if (n1.total_heuristics > n2.total_heuristics) 
                //     return 1; 
                // return 0; 

                return Double.compare(n1.manhattan_heuristics, n2.manhattan_heuristics);
            } 
        } 
    

        // PriorityQueue to store the nodes 
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(new NodeComparator());
//        Comparator<Node> heuristicComparator = Comparator.comparing(node -> node.robot_coordinates, Node::compareTo).thenComparingDouble(node -> node.total_heuristics);
//        TreeSet<Node> visitedNodes = new TreeSet<>(heuristicComparator);
        Set<Node> visitedNodes = new HashSet<>();
//        Set<Node> solutions = new HashSet<>();
        fringe.add(root);
        double start_time = System.currentTimeMillis();
        while(fringe.size() > 0) {
            Node currNode = fringe.poll();
            visitedNodes.add(currNode);
            if(!currNode.isSolved()){
                currNode.generate_children();
                for(Node child : currNode.children){
                    if (child != null)
                        if(!visitedNodes.stream().anyMatch(node -> Arrays.equals(node.robot_coordinates, child.robot_coordinates)))
                            fringe.add(child);
                }
            }
            else{
//                solutions.add(currNode);
            	double end_time = System.currentTimeMillis();
            	double exec_time = end_time - start_time;
            	System.out.println("Solution Found!\nVisited " + visitedNodes.size() + " Nodes in " + exec_time+" ms");
                return currNode;
            }
        }
//        System.out.println(solutions.toString());
        double end_time = System.currentTimeMillis();
    	double exec_time = end_time - start_time;
    	System.out.println("Solution Not Found!\nVisited " + visitedNodes.size() + " Nodes in " + exec_time+" ms");
		return null;
    }
	
	
	
	
	

    public static Node astar_euclidean(Node root){
        // Comparator class to compare the heuristic value 
        // while adding the node in PriorityQueue 
        class NodeComparator implements Comparator<Node> { 
            public int compare(Node n1, Node n2) 
            { 
                // if (n1.total_heuristics < n2.total_heuristics) 
                //     return -1; 
                // if (n1.total_heuristics > n2.total_heuristics) 
                //     return 1; 
                // return 0; 

                return Double.compare(n1.euclidean_heuristics, n2.euclidean_heuristics);
            } 
        } 
    

        // PriorityQueue to store the nodes 
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(new NodeComparator());
//        Comparator<Node> heuristicComparator = Comparator.comparing(node -> node.robot_coordinates, Node::compareTo).thenComparingDouble(node -> node.total_heuristics);
//        TreeSet<Node> visitedNodes = new TreeSet<>(heuristicComparator);
        Set<Node> visitedNodes = new HashSet<>();
//        Set<Node> solutions = new HashSet<>();
        fringe.add(root);
        double start_time = System.currentTimeMillis();

        while(fringe.size() > 0) {
            Node currNode = fringe.poll();
            visitedNodes.add(currNode);
            if(!currNode.isSolved()){
                currNode.generate_children();
                for(Node child : currNode.children){
                    if (child != null)
                        if(!visitedNodes.stream().anyMatch(node -> Arrays.equals(node.robot_coordinates, child.robot_coordinates)))
                            fringe.add(child);
                }
            }
            else{
//                solutions.add(currNode);
            	double end_time = System.currentTimeMillis();
            	double exec_time = end_time - start_time;
            	System.out.println("Solution Found!\nVisited " + visitedNodes.size() + " Nodes in " + exec_time+" ms");
                return currNode;
            }
        }
//        System.out.println(solutions.toString());
        double end_time = System.currentTimeMillis();
    	double exec_time = end_time - start_time;
    	System.out.println("Solution Not Found!\nVisited " + visitedNodes.size() + " Nodes in " + exec_time+" ms");
		return null;
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
        Node sol_euclidean = astar_euclidean(root);
        ArrayList<String> steps_euclidean = getSteps(sol_euclidean);
        String path = "";
        for(int i = steps_euclidean.size()-1; i >=0; i--) {
        	path+= steps_euclidean.get(i)+ ' ';
        }
        System.out.println("euclidean steps: " + sol_euclidean.generation);
        System.out.println("euclidean path:\n" + path);
        
        Node sol_manhattan = astar_manhattan(root);
        ArrayList<String> steps_manhattan = getSteps(sol_euclidean);
        path = "";
        for(int i = steps_manhattan.size()-1; i >=0; i--) {
        	path+= steps_manhattan.get(i)+ ' ';
        }
        System.out.println("manhattan steps: " + sol_manhattan.generation);
        System.out.println("manhattan path:\n" + path);

        
    }




	private static ArrayList<String> getSteps(Node sol) {
		// TODO Auto-generated method stub
		ArrayList<String> steps = new ArrayList<String>();
		Node curNode = sol;
		String step = "";

		while(curNode.parent != null) {
			step="("+curNode.robot_coordinates[1]+", "+curNode.robot_coordinates[0]+")";
			steps.add(step);
			curNode = curNode.parent;
		}
		return steps;
	}
    
}