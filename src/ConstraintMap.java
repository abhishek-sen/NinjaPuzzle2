import java.util.Arrays;

/**
 * Created by abhisheksen on 4/20/14.
 */

/*
*   Defining the constraints :
*
*   -1 : NO GO
*   >= 0 :    ALLOWED TO GO
*           > 0 : STANDING BLOCK PRESENT
*           = 0 : TOPPLED BLOCK PRESENT
*
* */


 public class ConstraintMap {

    private int[][] constraintMatrix;
    int nRows;
    int nCols;

    public ConstraintMap(int[] dim, int[] start, int[] end, int[] gridRowsStanding, int[] gridColsStanding, int[] crateSize, int[][] toppledGrids){

        this.nCols = dim[0];
        this.nRows = dim[1];
        this.constraintMatrix = new int[nRows][nCols];
        this.initializeConstraintMap(start, end, gridRowsStanding, gridColsStanding, crateSize, toppledGrids);

    }

    public ConstraintMap(ConstraintMap M){

        this.nRows = M.getRows();
        this.nCols = M.getCols();

        int[][] refMatrix = M.getConstraintMap();

        /* Deep cloning */
        this.constraintMatrix = new int[refMatrix.length][];
        for(int i = 0; i < refMatrix.length; i++)
            this.constraintMatrix[i] = refMatrix[i].clone();


    }




    private void initializeConstraintMap(int[] start, int[] end, int[] gridRowsStanding, int[] gridColsStanding, int[] crateSize, int[][] toppledGridSets){

        /* Initialize the constraint matrix to be unreachable */
        for (int i = 0; i < this.nRows; i++)
            for (int j = 0; j < this.nCols; j++)
                this.constraintMatrix[i][j] = -1;


        /* Starting point is reachable obviously */
        this.constraintMatrix[start[0]][start[1]] = 0;

        /* There is a destination grid cell*/
        this.constraintMatrix[end[0]][end[1]] = 0;

        for (int i = 0 ; i < gridRowsStanding.length ; i++)
            this.constraintMatrix[gridRowsStanding[i]][gridColsStanding[i]] = crateSize[i];

        for (int j = 0 ; j < toppledGridSets.length ; j++){

            /* Read the quintuple of grid location start and end points */

            int startRow = toppledGridSets[j][0]; int startCol = toppledGridSets[j][1];
            int endRow = toppledGridSets[j][2]; int endCol = toppledGridSets[j][3];

            /* Modify the constraint matrix appropriately */

            if(startRow == endRow)

                if(startCol <= endCol)
                    for (int k = startCol ; k <= endCol ; k++)
                        this.constraintMatrix[startRow][k] = 0;
                else
                    for (int k = startCol ; k >= endCol ; k--)
                        this.constraintMatrix[startRow][k] = 0;

            else
                if(startRow  < endRow)
                    for (int k = startRow ; k <= endRow ; k++)
                        this.constraintMatrix[k][startCol] = 0;
                else
                    for (int k = startRow ; k >= endRow ; k--)
                        this.constraintMatrix[k][startCol] = 0;



        }



    }


    /* Constraint map is updated when a crate is dropped in a particular 'direction'*/

    public void updateConstraintMatrix(int curRow, int curCol, int[] toppledGrids, int direction){

        // curRow, curCol : original location of the standing crate
        // toppledGrid : start and end grid locations of the toppled crate
        // direction : {0,1,2,3} - direction in which crate was toppled

        this.constraintMatrix[curRow][curCol] = 0;

        int startRow = toppledGrids[0]; int startCol = toppledGrids[1];
        int endRow = toppledGrids[2]; int endCol = toppledGrids[3];

        if(direction % 2 == 0){ // left or right

            if(direction == 0) // left
                for (int k = startCol ; k >= endCol ; k--)
                    this.constraintMatrix[startRow][k] = 0;
            else
                for (int k = startCol ; k <= endCol ; k++)
                    this.constraintMatrix[startRow][k] = 0;
        }
        else{ // up or down

            if(direction == 1) // down
                for (int k = startRow ; k <= endRow ; k++)
                    this.constraintMatrix[k][startCol] = 0;
            else
                for (int k = startRow ; k >= endRow ; k--)
                    this.constraintMatrix[k][startCol] = 0;


        }

    }

    /* helpers to check if box can be toppled in a particular direction */

    private boolean canDropLeft(int curCell, int crateSize){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(curCell, this.nCols);

        int curRow = coordinates[0];
        int curCol = coordinates[1];

        if(curCol < 0 + crateSize )
            return false;

        int cellCount = 0;

        while(cellCount < crateSize){
            curCol--;
            if(this.constraintMatrix[curRow][curCol] >= 0)
                return false;
            cellCount++;
        }

        return true;
    }

    private boolean canDropRight(int curCell, int crateSize){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(curCell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];


        if(curCol >= this.nCols - crateSize )
            return false;

        int cellCount = 0;

        while(cellCount < crateSize){
            curCol++;
            if(this.constraintMatrix[curRow][curCol] >= 0)
                return false;
            cellCount++;
        }

        return true;
    }

    private boolean canDropUp(int curCell, int crateSize){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(curCell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];


        if(curRow < 0 + crateSize)
            return false;

        int cellCount = 0;
        while(cellCount < crateSize){
            curRow--;

            if(this.constraintMatrix[curRow][curCol] >= 0)
                return false;
            cellCount++;
        }


        return true;
    }

    private boolean canDropDown(int curCell, int crateSize){


        int[] coordinates;
        coordinates = MapUtils.cellToCoords(curCell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];

        if(curRow >= this.nRows - crateSize)
            return false;

        int cellCount = 0;
        while(cellCount < crateSize){
            curRow++;

            if(this.constraintMatrix[curRow][curCol] >= 0)
                return false;
            cellCount++;
        }

        return true;
    }

    /* Used to check the direction in which the crate can be dropped to generate new path for the bot */

    public boolean[] getExpandableDirections(int curCell, int crateSize){

        /* 4 Boolean flags for left, right, up and down */
        boolean[] expansionFlags = new boolean[4];

        expansionFlags[0] = canDropLeft(curCell, crateSize);
        expansionFlags[1] = canDropDown(curCell, crateSize);
        expansionFlags[2] = canDropRight(curCell, crateSize);
        expansionFlags[3] = canDropUp(curCell, crateSize);


        return expansionFlags;
    }

    private int getRows(){
        return this.nRows;
    }

    private int getCols(){
        return this.nCols;
    }

    public int[][] getConstraintMap(){
        return this.constraintMatrix;
    }

    public int getCrateSize(int cell){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(cell, this.nCols);

        return this.constraintMatrix[coordinates[0]][coordinates[1]];

    }

    public int getCrateSize (int row, int col){

        return this.constraintMatrix[row][col];

    }


}
