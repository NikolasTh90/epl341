package hw1;
public class Grid {
    public int grid[][];
    public int start[];
    public int goal[];

    public Grid(int description[][], int start[], int goal[]) {
        this.grid = description;
        this.start = start;
        this.goal = goal;
        this.grid[start[0]][start[1]] = -1; // set start position as -1
        this.grid[goal[0]][goal[1]] = -2; // set goal position as -2
    }

    // public int[][] getGrid() {
    //     return this.grid;
    // }

    // public 




}
