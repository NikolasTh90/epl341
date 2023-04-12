package hw2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

class MVC {
    static int  vertices;
    static float density;
    static int edges;
    static int[][] adjacency_matrix;

    public static Scanner getFileScanner(String fileName){
        try {
            File file = new File(fileName);
            Scanner file_scan = new Scanner(file);
            return file_scan;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
    }

    public static int[][] generateRandomAdjancencyMatrix(int vertices, float density) {
        adjacency_matrix = new int[vertices][vertices];
        edges = Math.round((density * vertices * (vertices - 1)) / 2);
        int edge_count = 0;
        do {
            int row = (int)(Math.random() * vertices);
            int col = (int)(Math.random() * vertices);
            if(adjacency_matrix[col][row] == 0) {
                adjacency_matrix[col][row] = 1;
                adjacency_matrix[row][col] = 1;
                edge_count++;
            }
        
        }while(edge_count != edges);
        
        return adjacency_matrix;
    }

    public static void generateSolverInputFile(int[][] adjacency_matrix) {
        ArrayList<int[]> clauses = createClauses(adjacency_matrix);
        String out = "";
        for (int[] clause : clauses)
            out += "h " + clause[0] + " " + clause[1] + " 0\n";
        for (int node=0; node<vertices; node++)
            out += "1 -" + (node+1) + " 0\n";
        try {
            FileWriter myWriter = new FileWriter("DIMACS.wcnf");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Could not open DIMACS.wcnf");
            e.printStackTrace();
        }

    }

    public static int[][] readFile(String fileName) {
        Scanner file_scan = getFileScanner(fileName);
        vertices = file_scan.nextInt();
        density = file_scan.nextFloat();
        adjacency_matrix = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                adjacency_matrix[i][j] = file_scan.nextInt();
            }
        }
        file_scan.close();
        return adjacency_matrix;

    }


    public static ArrayList<int[]> createClauses(int[][] adjacency_matrix) {
        ArrayList<int[]> clauses = new ArrayList<int[]>();
            for (int i=0; i<adjacency_matrix.length; i++){
                for (int j=0; j<=i; j++){
                    if (adjacency_matrix[i][j] == 1){
                        int clause[] = new int[2];
                        clause[0] = i+1;
                        clause[1] = j+1;
                        clauses.add(clause);
                    }
                }
            }
            return clauses;
        }

        
    public static void run_solver() {
        String command = "./cashwmaxsatcoreplus DIMACS.wcnf";
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // Get process output
            InputStream inputStream = process.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

            scanner.close();
            System.out.println("Command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public static void displayMatrix() {
        System.out.println("Graph Information:");
        System.out.println("Number of Nodes: " + vertices);
        System.out.println("Density: " + density);
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                System.out.print(adjacency_matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Edges:");
        for (int i = 0; i < vertices; i++) {
            for (int j = i + 1; j < vertices; j++) {
                if (adjacency_matrix[i][j] == 1) {
                    System.out.println("[" + (i + 1) + ", " + (j + 1) + "]");
                }
            }
        }
    }
        
        
        


    public static void main(String[] args) {
        if ( !args[0].equals("0") && !args[0].equals("1") ) {
            System.out.println("Invalid arguments. Enter 0 or 1 to provide an adjency matrix or to generate a random matrix.");
            return;
        }


        String fileName = "";
        Scanner stdin_scan = new Scanner(System.in);
        if(args[0].equals("0")){
            System.out.println("Enter the file name: ");
            fileName = stdin_scan.nextLine();
            adjacency_matrix = readFile(fileName);
        }
        else {
            System.out.println("Enter number of vectices: ");
            vertices = stdin_scan.nextInt();
            System.out.println("Enter density: ");
            density = stdin_scan.nextFloat();
            adjacency_matrix = generateRandomAdjancencyMatrix(vertices, density);
        }
        generateSolverInputFile(adjacency_matrix);
        displayMatrix();
        run_solver();

    stdin_scan.close();
        }
}