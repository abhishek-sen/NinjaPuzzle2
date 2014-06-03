import java.io.IOException;

/**
 * Created by abhisheksen on 4/19/14.
 */

/*
* This is the Entry point to the program.
* */
public class Main {

    public static void main(String[] args) throws IOException{


        /* Read the Config file for the game */

        String configFile = new String("/Users/abhisheksen/SourceCodeLibrary/NinjaPuzzle2/config/board3.json");
        GameConfigReader jReader = new GameConfigReader(configFile);
        JsonFormat data = jReader.getJsonData();






        int[] board = data.getBoard();//{6,5};
        int[][] standing_crates = data.getStanding_crates();//{{0,1,2}, {1,3,2}, {2,0,4}, {3,4,4}};
        int[][] toppled_crates = data.getToppled_crates(); //{{4,3,4,4}};

        int[] start = data.getStart();//{3,4};
        int[] end = data.getEnd();//{0,4};


        //int[] gridRowsStanding = {0, 1, 2, 3};
        //int[] gridColsStanding = {1, 3, 0, 4};
        //int[] crateSizes = { 2, 2, 4, 4};

        int[] gridRowsStanding = DataParserUtils.getStandingRows(standing_crates); //
        int[] gridColsStanding = DataParserUtils.getStandingColumns(standing_crates); //
        int[] crateSizes = DataParserUtils.getCrateHeights(standing_crates); //

        GameEngine engine = new GameEngine(board, start, end, gridRowsStanding, gridColsStanding, crateSizes, toppled_crates);


        String outFile = new String("/Users/abhisheksen/SourceCodeLibrary/NinjaPuzzle2/answers/board3_solution.json");

        JsonWriterWrapper writer = new JsonWriterWrapper(outFile);
        writer.getWriter().beginObject();
        writer.getWriter().name("Path");
        writer.getWriter().beginArray();

        /* Pass along the writer object to write the path in the output file */
        engine.runGameEngine(writer);
        writer.getWriter().endArray();
        writer.getWriter().endObject();
        writer.getWriter().close();


    }

}
