import java.io.IOException;
import java.util.*;

/**
 * Created by abhisheksen on 4/20/14.
 */

/* *
*
* This is a data-structure to store and manage path
*
* */

// Update the path every-time a new node is explored by the bot.

// Eventually the bot will printout the path when it reaches its destination.

public class PathMap {

    private ArrayList<Integer> pathList;

    public PathMap(){

        pathList = new ArrayList<Integer>();

    }

    public PathMap( PathMap pMap){
        pathList = new ArrayList<Integer>(pMap.getPath());
    }

    public ArrayList<Integer> getPath(){
        return this.pathList;
    }

    public void addNode (Integer pathNode){
        pathList.add(pathNode);
    }

    public void printPath(int nCols){

        int[] coordinates;
        Iterator it = this.pathList.iterator();

        System.out.println("-------------------");

        while(it.hasNext()){
            Integer node = (Integer)it.next();

            coordinates = MapUtils.cellToCoords(node, nCols);

            int curRow = coordinates[0];
            int curCol = coordinates[1];

            System.out.println(curRow + "," + curCol);
        }

        System.out.println("-------------------");

    }

    public void printPath(int nCols, JsonWriterWrapper writer) throws IOException{

        int[] coordinates;
        Iterator it = this.pathList.iterator();



        writer.getWriter().beginArray();

        while(it.hasNext()){
            Integer node = (Integer)it.next();

            coordinates = MapUtils.cellToCoords(node, nCols);

            int curRow = coordinates[0];
            int curCol = coordinates[1];

            writer.getWriter().beginArray();
            writer.getWriter().value(curRow + ", " + curCol);
            writer.getWriter().endArray();

            //System.out.println(curRow + "," + curCol);
        }
        writer.getWriter().endArray();
        //writer.getWriter().endObject();

       // writer.getWriter().flush();





    }


}
