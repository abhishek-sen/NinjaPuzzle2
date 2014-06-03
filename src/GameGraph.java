/**
 * Created by abhisheksen on 4/19/14.
 */
import org.jgrapht.alg.DirectedNeighborIndex;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;



/* Model the space as a graph with grid cells as vertices. Two vertices are connected by a directed edge
* if and only if the bot can move from the first grid cell to the second grid cell */



 public class GameGraph {


    /* Directed Graph */

    DefaultDirectedGraph<Integer, DefaultEdge> gridGraph;

    /* Indexed neighbourhood for fast neighbour access */

    DirectedNeighborIndex<Integer, DefaultEdge> neighborIndex;


    private int nRows;
    private int nCols;


    /* Constructors : */

    public GameGraph(int[] dim, int[][] toppledGrid, ConstraintMap cMap){

        this.nCols = dim[0];
        this.nRows = dim[1];

        initializeGridGraph(toppledGrid, cMap);
        neighborIndex = new DirectedNeighborIndex<Integer, DefaultEdge>(gridGraph);

    }

     /* This is for cloning the original graph and other auxiliary data-structures */
     public GameGraph(GameGraph newGraph){


         this.nRows = newGraph.getRows();
         this.nCols = newGraph.getCols();

         gridGraph = newGraph.cloneGraph();
         neighborIndex = new DirectedNeighborIndex<Integer, DefaultEdge>(gridGraph);



     }






    private void initializeGridGraph(int[][] toppledGrids, ConstraintMap cMap){

        this.gridGraph = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);

        /* Add all the vertices. New vertices will not be added in the graph at any other time */

        for (int i = 0 ; i < nRows ; i++)
            for (int j = 0; j < nCols ; j++)
                this.gridGraph.addVertex(new Integer( i*nCols + j));

        /* Connect edge between adjoining grid cells if both contain crates */

        for (int i = 0 ; i < nRows; i++){
            for (int j = 0 ; j <nCols; j++){

                int curEntry = i * this.nCols + j;

                if(i+1 < nRows){ // Bottom

                    if(cMap.getCrateSize(i+1,j) >=0 && cMap.getCrateSize(i,j)>=0 ){
                        int neighborEntry = (i+1)*this.nCols + j;
                        this.gridGraph.addEdge(new Integer(curEntry), new Integer(neighborEntry));
                    }
                }

                if(j+1 < nCols){ // Right

                    if(cMap.getCrateSize(i,j+1) >=0 && cMap.getCrateSize(i,j)>=0){
                        int neighborEntry = (i)*this.nCols + j+1;
                        this.gridGraph.addEdge(new Integer(curEntry), new Integer(neighborEntry));
                    }
                }

                if(i-1 >= 0){ // Top

                    if(cMap.getCrateSize(i-1,j) >=0 && cMap.getCrateSize(i,j)>=0){
                        int neighborEntry = (i-1)*this.nCols + j;
                        this.gridGraph.addEdge(new Integer(curEntry), new Integer(neighborEntry));
                    }
                }

                if(j-1 >= 0){ // Bottom

                    if(cMap.getCrateSize(i,j-1) >=0 && cMap.getCrateSize(i,j)>=0){
                        int neighborEntry = i *this.nCols + j-1;
                        this.gridGraph.addEdge(new Integer(curEntry), new Integer(neighborEntry));
                    }
                }






            }
        }

        /* Add edges corresponding to the nodes which are reachable because of toppled crates */

        for (int j = 0 ; j < toppledGrids.length ; j++){

            /* Read the quintuple of grid location start and end points */

            int startRow = toppledGrids[j][0]; int startCol = toppledGrids[j][1];
            int endRow = toppledGrids[j][2]; int endCol = toppledGrids[j][3];
            int prevEntry = startRow * this.nCols + startCol;

            if(startRow == endRow)
                for (int k = startCol+1 ; k <= endCol ; k++){
                    int newEntry = startRow * this.nCols + k;
                    this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));
                    prevEntry = newEntry;
                }
            else
                for (int k = startRow+1 ; k <= endRow ; k++){
                    int newEntry = k * this.nCols + startCol;
                    this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));
                    prevEntry = newEntry;
                }


        }



    }





    /* *
    *
    * Update the graph by adding new edges.
    *
    * This will be typically invoked before making new moves
    *
    * */

     public void updateGraph(int row, int col, ArrayList<Integer> newRows, ArrayList<Integer> newCols, ConstraintMap cMap, VisitedMap vMap, int direction){

        /* *
        *
        *  Row : current Row location of the bot
        *  Col : current Col location of the bot
        *  newRows : new row locations that becomes accessible
        *  newCols : new col locations that becomes accessible
        *  cMap : Constraint Map of the world
        *  vMap : Map of the cells already visited by the Bot
        *  direction : in which the graph will be expanded
        *
        * */

         Integer prevRow = new Integer(row);
         Integer prevCol = new Integer(col);
         Integer prevEntry = prevRow * this.nCols + prevCol;

         Iterator itRow = newRows.iterator();
         Iterator itCol = newCols.iterator();

         Integer curRow = new Integer(0);
         Integer curCol = new Integer(0);

        /* We assume here that itRow and itCol has same number of entries*/
         while(itRow.hasNext()){

             curRow = (Integer) itRow.next();
             curCol = (Integer) itCol.next();

             Integer newEntry = curRow*this.nCols + curCol;
             this.gridGraph.addEdge(prevEntry, newEntry);

            /* Connect to existing nodes satisfying appropriate constraints*/
             if(direction % 2 == 0){

                 this.connectToTop(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 this.connectToBottom(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);

             }else{

                 this.connectToLeft(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 this.connectToRight(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);

             }

             prevEntry = newEntry;
             prevRow = curRow;
             prevCol = curCol;


         }



        /* Check for other connections */

         if(direction % 2 == 0){

             this.connectToTop(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
             this.connectToBottom(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);

         }else{

             this.connectToLeft(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
             this.connectToRight(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);

         }

         switch(direction){
             case 0:
                 this.connectToLeft(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 break;
             case 1:
                 this.connectToBottom(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 break;
             case 2:
                 this.connectToRight(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 break;
             case 3:
                 this.connectToTop(prevEntry.intValue(), prevRow.intValue(), prevCol.intValue(), cMap, vMap);
                 break;

         }



     }

   ///////////////////////////
   /* Other helper methods */
   ///////////////////////////

    private DirectedNeighborIndex getNeighborIndex(){

         return this.neighborIndex;

     }

    public List<Integer> getNeighbours(Integer vertexID){

        return neighborIndex.successorListOf(vertexID);

    }



    public DefaultDirectedGraph <Integer, DefaultEdge> getGridGraph(){

        return this.gridGraph;

    }

     public int getRows(){
         return this.nRows;
     }

     public int getCols(){
         return this.nCols;
     }













    private void connectToTop(int prevEntry, int prevRow, int prevCol,  ConstraintMap cMap, VisitedMap vMap){

        if(MapUtils.hasTop(prevEntry, this.nRows, this.nCols)){

            if( cMap.getCrateSize(prevRow-1, prevCol) >= 0 && !vMap.isVisited(prevRow-1, prevCol)){

                int newEntry = (prevRow-1) * this.nCols + (prevCol);
                this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));

            }


        }


    }

    private void connectToBottom(int prevEntry, int prevRow, int prevCol,  ConstraintMap cMap, VisitedMap vMap){

        if(MapUtils.hasBottom(prevEntry, this.nRows, this.nCols)){

            if( cMap.getCrateSize(prevRow+1, prevCol) >= 0 && !vMap.isVisited(prevRow+1, prevCol)){

                int newEntry = (prevRow+1) * this.nCols + (prevCol);
                this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));

            }


        }

    }

    private void connectToLeft(int prevEntry, int prevRow, int prevCol,  ConstraintMap cMap, VisitedMap vMap){

        if(MapUtils.hasLeft(prevEntry, this.nRows, this.nCols)){

            if( cMap.getCrateSize(prevRow , prevCol-1) >= 0 && !vMap.isVisited(prevRow, prevCol-1)){

                int newEntry = prevRow * this.nCols + (prevCol-1);
                this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));

            }


        }

    }

    private void connectToRight(int prevEntry, int prevRow, int prevCol,  ConstraintMap cMap, VisitedMap vMap){

        if(MapUtils.hasRight(prevEntry, this.nRows, this.nCols)){

            if( cMap.getCrateSize(prevRow, prevCol+1) >= 0 && !vMap.isVisited(prevRow, prevCol+1)){

                int newEntry = prevRow * this.nCols + (prevCol+1);
                this.gridGraph.addEdge(new Integer(prevEntry), new Integer(newEntry));

            }


        }

    }


    public DefaultDirectedGraph <Integer, DefaultEdge> cloneGraph(){


        DefaultDirectedGraph <Integer, DefaultEdge> newGraph = new DefaultDirectedGraph <Integer, DefaultEdge>(DefaultEdge.class);
        Set<Integer> vSet = this.gridGraph.vertexSet();

        /* Add vertices */
        Iterator it = vSet.iterator();
        while(it.hasNext())
            newGraph.addVertex((Integer)it.next());

        /* Add edges */
        it = vSet.iterator();

        while(it.hasNext()){
            Integer v = (Integer)it.next();
            List<Integer> neighbors = this.neighborIndex.successorListOf(v);

            Iterator tmpIt = neighbors.iterator();
            while(tmpIt.hasNext()){
                Integer n = (Integer)tmpIt.next();
                newGraph.addEdge(v,n);
            }

        }



        return newGraph;
    }

















}
