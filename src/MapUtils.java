/**
 * Created by abhisheksen on 4/21/14.
 */
public class MapUtils {



    /* Vertex ID to coordinate */
    public static int[] cellToCoords(int cell, int nCols){

        int[] coordinates = new int[2];
        int curRow = (int) Math.floor( (double) cell/ (double) nCols);
        int curCol = cell % nCols;

        coordinates[0] = curRow;
        coordinates[1] = curCol;

        return coordinates;


    }

    public static boolean hasLeft(int cell, int nRows, int nCols){

        int[] pt;
        pt = MapUtils.cellToCoords(cell, nCols);
        int curRow = pt[0];
        int curCol = pt[1];

        if(curCol > 0)
            return true;

        return false;
    }

    public static boolean hasRight(int cell, int nRows, int nCols){

        int[] pt;
        pt = MapUtils.cellToCoords(cell, nCols);
        int curRow = pt[0];
        int curCol = pt[1];

        if(curCol < nCols-1 )
            return true;

        return false;
    }

    public static boolean hasTop(int cell, int nRows, int nCols){
        int[] pt;
        pt = MapUtils.cellToCoords(cell, nCols);
        int curRow = pt[0];
        int curCol = pt[1];

        if(curRow > 0)
            return true;

        return false;

    }

    public static boolean hasBottom(int cell, int nRows, int nCols){
        int[] pt;
        pt = MapUtils.cellToCoords(cell, nCols);
        int curRow = pt[0];
        int curCol = pt[1];

        if(curRow < nRows-1 )
            return true;

        return false;
    }




}
