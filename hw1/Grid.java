package hw1;
import java.util.Scanner;
public class Grid {
    private int grid[][];

    public Grid(int x, int y, int description[][], int start[], int goal[]) {
        this.grid = description;
        this.grid[start[0]][start[1]] = -1; // set start position as -1
        this.grid[goal[0]][goal[1]] = -2; // set goal position as -2
    }

    

}
