/**
 * Created by abhisheksen on 4/22/14.
 */
public class DataParserUtils {

    public static int[] getStandingRows(int[][] crateStandingPositions){


        int[] rowPositions = new int[crateStandingPositions.length - 1]; // -1 is because of internal formatting of GSon
        for (int i = 0 ; i < crateStandingPositions.length - 1; i++)
            rowPositions[i] = crateStandingPositions[i][0];



        return rowPositions;


    }

    public static int[] getStandingColumns(int[][] crateStandingPositions){

        int[] colPositions = new int[crateStandingPositions.length - 1];
        for (int i = 0 ; i < crateStandingPositions.length - 1; i++)
            colPositions[i] = crateStandingPositions[i][1];

        return colPositions;


    }

    public static int[] getCrateHeights(int[][] crateStandingPositions){

        int[] crateHeights = new int[crateStandingPositions.length - 1];
        for (int i = 0 ; i < crateStandingPositions.length - 1; i++)
            crateHeights[i] = crateStandingPositions[i][2];

        return crateHeights;


    }

}
