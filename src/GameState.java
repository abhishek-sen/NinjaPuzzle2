import java.io.IOException;
import java.util.*;

/**
 * Created by abhisheksen on 4/20/14.
 */
public class GameState {

    /* These data structures together maintain the game state */
    private GameGraph gGraph;
    private ConstraintMap cMap;
    private VisitedMap vMap;
    private PathMap pMap;

    /* Start and End Coordinates in the grid */

    private int[] start;
    private int[] end;

    /* Store the number of Rows and Cols of the board for book-keeping purposes*/

    private int nRows;
    private int nCols;


    /* Constructor for game initialization */

    public GameState(int[] dim, int[] start, int[] end, int[] gridRowsStanding, int[] gridColsStanding, int[] weights, int[][] toppledGrids){

        cMap = new ConstraintMap(dim, start, end, gridRowsStanding, gridColsStanding, weights, toppledGrids);
        gGraph = new GameGraph(dim, toppledGrids, this.cMap);

        vMap = new VisitedMap(dim);
        pMap = new PathMap();

        this.nCols = dim[0];
        this.nRows = dim[1];

        this.start = start;
        this.end = end;

    }

    /* overloaded constructor for game time instantiation */

    public GameState(GameState GS){


        /* Ensure that new instances are deep-clones of passed objects */
        this.gGraph = new GameGraph(GS.getGameGraph());
        this.cMap = new ConstraintMap(GS.getConstraintMatrix());
        this.vMap = new VisitedMap(GS.getVisitedMap());
        this.pMap = new PathMap(GS.getPathMap());

        // other parameters for book-keeping
        this.nRows = GS.getRows();
        this.nCols = GS.getCols();
        this.end = GS.getEnd();

    }

     /* Game Engine calls this method to start the game */

    public void bootstrap(JsonWriterWrapper writer) throws IOException{

        Integer startNode = start[0] * this.nCols + start[1];
        int crateSize = this.cMap.getCrateSize(start[0], start[1]);
        this.ExploreGame(startNode, crateSize, writer);

        return;

    }


    /* Call this method if there is standing crate in the current grid location*/

    public void ExploreGame(Integer startNode, int crateSize, JsonWriterWrapper writer) throws IOException{



        if(crateSize > 0){

            /* Expand the set of possibilities */

            boolean[] directions = this.getConstraintMatrix().getExpandableDirections(startNode, crateSize);
            for (int dirInd = 0 ; dirInd < directions.length; dirInd++)
                if(directions[dirInd]){

                    GameState newGS = new GameState(this);
                    newGS.ExpandGame(startNode, crateSize, dirInd, writer);
                }
        }


        /* Update Visited Status of startNode */

        this.updateVisitedMap(startNode);
        this.addPathNode(startNode);


        /* Get the set of neighbours of current Node from the gameGraph */
        List<Integer> neighborList = this.gGraph.getNeighbours(startNode);

        /* Move to each of the neighbours and explore the remaining graph */
        Iterator<Integer> it = neighborList.iterator();

        while(it.hasNext()){

            Integer neighborID = it.next();



            /* No need to proceed further is destination node is found. Print path and return */
            if(isEndNode(neighborID)){
                this.addPathNode(neighborID);
                this.printPath(this.nCols);
                this.printPath(this.nCols, writer);

                return;
            }

            int neighborCrateSize = this.cMap.getCrateSize(neighborID);

            if(! this.isVisited(neighborID)){

                GameState GS = new GameState(this);

                GS.ExploreGame(neighborID, neighborCrateSize, writer);
            }
        }







    }

    /* *
    *
    * Call this if there is a standing crate in the current grid location.
    * Designed in this way so that we can isolate the behaviour of graph expansion
    * in future version of this program (in case)
    *
    * */

    public void ExpandGame(int startNode , int crateSize, int expDirection, JsonWriterWrapper writer) throws IOException{
        // expDirection : { 0, 1, 2, 3 }


        int curNode = startNode;
        int[] coordinates;
        coordinates = MapUtils.cellToCoords(curNode, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];



        /* Update Constraint Matrix */

        int[] gridTuple = GameState.generateGridTuple(curRow, curCol, crateSize, expDirection);
        this.updateConstraintMatrix(curRow, curCol, gridTuple, expDirection);

        /* Update the Game Graph */

        ArrayList<Integer> colList = GameState.getColsFromTuple(gridTuple, expDirection);
        ArrayList<Integer> rowList = GameState.getRowsFromTuple(gridTuple, expDirection);

        this.updateGraph(curRow, curCol, rowList, colList, expDirection);

        /* *
        *
        * CrateSize = 0 because we have just toppled the box
        * Now continue exploring along this path
        *
        * */


        if(! this.isVisited(startNode)) // being defensive
            this.ExploreGame(startNode, 0, writer);

    }

    ////////////////////////////////
    /* Other helper functions */
    ////////////////////////////////

    public ConstraintMap getConstraintMatrix(){
        return this.cMap;
    }

    public GameGraph getGameGraph(){
        return this.gGraph;
    }

    public VisitedMap getVisitedMap(){
        return this.vMap;
    }

    public PathMap getPathMap(){
        return this.pMap;
    }

    public int getRows(){
        return this.nRows;
    }

    public int getCols(){
        return this.nCols;
    }

    public int[] getEnd(){
        return this.end;
    }





    private void updateConstraintMatrix(int curRow, int curCol, int[] gridTuple, int direction){

        this.cMap.updateConstraintMatrix(curRow, curCol, gridTuple, direction);

    }

    private boolean isVisited(int cell){

        return this.vMap.isVisited(cell);
    }

    private void updateVisitedMap(int cell){

        this.vMap.updateVisitedMap(cell);

    }

    private void updateGraph(int row, int col, ArrayList<Integer> newRows, ArrayList<Integer> newCols, int direction){

        this.gGraph.updateGraph(row, col, newRows, newCols, this.cMap, this.vMap, direction);

    }

    private void addPathNode(Integer pathNode){
        this.pMap.addNode(pathNode);
    }

    private void printPath(int nCols, JsonWriterWrapper writer) throws IOException{
        this.pMap.printPath(nCols, writer);
    }

    private void printPath(int nCols){
        this.pMap.printPath(nCols);
    }

    private boolean isEndNode(int cell){

        int[] coordinates;
        coordinates = MapUtils.cellToCoords(cell, this.nCols);
        int curRow = coordinates[0];
        int curCol = coordinates[1];

        if(curRow == this.end[0] && curCol == this.end[1])
            return true;

        return false;
    }


    /* *
    *
    *  Generate a tuple of start and end grid points when a new crate is toppled
    *  This will passed on as a parameter to update ConstraintMatrix
    *
    *  */

    private static int[] generateGridTuple(int startRow, int startCol, int craterSize, int direction){

        int endRow = startRow;
        int endCol = startCol;

        switch(direction){
            case 0: //left
                endCol = startCol - craterSize;
                startCol = startCol - 1;

                break;
            case 1: //down
                endRow = startRow + craterSize;
                startRow = startRow + 1;
                break;
            case 2: //right
                endCol = startCol + craterSize;
                startCol = startCol + 1;
                break;

            case 3: //up
                endRow = startRow - craterSize;
                startRow = startRow - 1;

                break;

            default:
                break;

        }

        int[] tuple = new int[4];
        tuple[0] = startRow; tuple[1] = startCol;
        tuple[2] = endRow; tuple[3] = endCol;

        return tuple;

    }

    private static ArrayList<Integer> getRowsFromTuple(int[] tuple, int direction){

        int startRow = tuple[0];
        int endRow = tuple[2];

        int startCol = tuple[1];
        int endCol = tuple[3];

        ArrayList<Integer> rowList = new ArrayList<Integer>();

        if(direction%2 == 0)

            if(direction == 0)
                for (int k = startCol ; k >= endCol ; k--)
                    rowList.add(startRow);
            else
                for (int k = startCol ; k <= endCol ; k++)
                    rowList.add(startRow);
        else

            if(direction == 1)
                for (int k = startRow ; k <= endRow ; k++)
                    rowList.add(k);
            else
                for (int k = startRow ; k >= endRow ; k--)
                    rowList.add(k);



        return rowList;

    }

    private static ArrayList<Integer> getColsFromTuple(int[] tuple, int direction){

        int startRow = tuple[0];
        int endRow = tuple[2];

        int startCol = tuple[1];
        int endCol = tuple[3];

        ArrayList<Integer> colList = new ArrayList<Integer>();

        if(direction%2 == 1)

            if(direction == 1)
                for (int k = startRow ; k <= endRow ; k++)
                    colList.add(startCol);
            else
                for (int k = startRow ; k >= endRow ; k--)
                    colList.add(startCol);

        else
            if(direction == 0)
                for (int k = startCol ; k >= endCol ; k--)
                    colList.add(k);
            else
                for (int k = startCol ; k <= endCol ; k++)
                    colList.add(k);



        return colList;

    }




}
