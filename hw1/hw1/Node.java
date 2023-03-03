package hw1;
import java.util.Arrays;
public class Node {
    int robot_coordinates[];
    int goal_coordinates[];
    Grid grid;
    int manhattan_heuristics;
    double euclidean_heuristics;
    int generation;
    double total_heuristics;
    Node children[];
    Node parent;

    

    public Node(Grid grid){
        this.grid = grid;
        this.robot_coordinates = grid.start;
        this.goal_coordinates = grid.goal;
        
        parent = null;
        euclidean_heuristics = calculate_euclidean_heuristics();
        manhattan_heuristics = calculate_manhattan_heuristics();
        generation = 0;   
        total_heuristics = euclidean_heuristics + generation + manhattan_heuristics;
        children = new Node[4];
        
    }

    public Node(int robot_coordinates[], Node parent){
        this.parent = parent;
        this.generation = parent.generation+1;
        this.robot_coordinates = robot_coordinates;
        this.goal_coordinates = parent.goal_coordinates;
        this.grid = parent.grid;
        euclidean_heuristics = calculate_euclidean_heuristics();
        manhattan_heuristics = calculate_manhattan_heuristics();
        total_heuristics = euclidean_heuristics+manhattan_heuristics+generation;
        children = new Node[4];


    }


    private double calculate_euclidean_heuristics(){
        double dx = Math.pow(robot_coordinates[0]- goal_coordinates[0], 2);
        double dy = Math.pow(robot_coordinates[1]- goal_coordinates[1], 2);
        return Math.sqrt(dx+dy);

    }

    private int calculate_manhattan_heuristics(){
        int dx = Math.abs(goal_coordinates[0] - robot_coordinates[0]);
        int dy = Math.abs(goal_coordinates[1] - robot_coordinates[1]);
        return dx + dy;
    }

    public void generate_children(){
        int right[] = {robot_coordinates[0], robot_coordinates[1]+1};
        int left[] = {robot_coordinates[0], robot_coordinates[1]-1};
        int up[] = {robot_coordinates[0]-1, robot_coordinates[1]};
        int down[] = {robot_coordinates[0]+1, robot_coordinates[1]};
        if(isValidMove(right)) {
            children[0] = new Node(right, this);
//        	children[0].grid.grid [ children[0].robot_coordinates[1] ][ children[0].robot_coordinates[0] ] = -1;
        }
        else
            children[0] = null;
        if(isValidMove(left))
            children[1] = new Node(left, this);
        else
            children[1] = null;
        if(isValidMove(up))
            children[2] = new Node(up, this);
        else
            children[2] = null;
        if(isValidMove(down))
            children[3] = new Node(down, this);
        else
            children[3] = null;
        
    }


    private boolean isValidMove(int coordinates[]){
        int maxY = this.grid.grid.length - 1;
        int maxX = this.grid.grid[0].length - 1;
        
//        System.out.println(coordinates[1] + " " + coordinates[0] );
        if(coordinates[0]>maxY || coordinates[1]>maxX)
            return false;
        if(coordinates[0]<0 || coordinates[1]<0)
            return false;
        if(grid.grid[coordinates[0]][coordinates[1]] == 1 || grid.grid[coordinates[0]][coordinates[1]] == -1)
            return false;
        return true;


    }

    public boolean isSolved(){
    	
        return Arrays.equals(this.robot_coordinates, this.goal_coordinates);
    }

}
