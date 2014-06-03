/**
 * Created by abhisheksen on 4/20/14.
 */

/* Data structure to keep track of the grid cells already visited by the bot */

/* Visited [i][j] = 1 if the grid (i,j) is visited. Otherwise it is 0. */

public class VisitedMap {

    private int[][] visitedMatrix;
    private int nRows;
    private int nCols;


    public VisitedMap(int[] dim){

        this.nCols = dim[0];
        this.nRows = dim[1];
        this.visitedMatrix = new int[nRows][nCols];



    }

    public VisitedMap ( VisitedMap V){

        int[][] refMatrix = V.getVisitedMap();

        this.visitedMatrix = new int[refMatrix.length][];
        for(int i = 0; i < refMatrix.length; i++)
            this.visitedMatrix[i] = refMatrix[i].clone();


        this.nRows = V.getRows();
        this.nCols = V.getCols();

    }

    public int[][] getVisitedMap(){

        return this.visitedMatrix;

    }

    public void updateVisitedMap(int Cell){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(Cell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];

        this.visitedMatrix[curRow][curCol] = 1;

    }

    public boolean isVisited(int Cell){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(Cell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];

        return (this.visitedMatrix[curRow][curCol] == 1) ? true : false ;

    }

    public boolean isVisited(int curRow, int curCol){
        return (this.visitedMatrix[curRow][curCol] == 1) ? true : false ;
    }



    public int getRows(){
        return nRows;
    }

    public int getCols(){
        return nCols;
    }






}
