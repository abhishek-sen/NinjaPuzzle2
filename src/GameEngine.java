import java.io.IOException;

/**
 * Created by abhisheksen on 4/20/14.
 */
public class GameEngine {

    private GameState gameState;

    /* Initialize game engine */
    public GameEngine(int[] dim, int[] start, int[] end, int[] gridRowsStanding, int[] gridColsStanding, int[] weights, int[][] toppledGrids){
        gameState = new GameState(dim, start, end, gridRowsStanding, gridColsStanding, weights, toppledGrids);
    }



    /* This will start playing the game */
    public void runGameEngine(JsonWriterWrapper writer) throws IOException{
        gameState.bootstrap(writer);
    }




}
