/**
 * Created by abhisheksen on 4/23/14.
 */
import java.util.List;


public class JsonFormat {

    public int[] start;
    public int[] end;
    public int[] board;
    public int[][] standing_crates;
    public int[][] toppled_crates;

    public int[] getStart(){
        return this.start;
    }

    public int[] getEnd(){
        return this.end;
    }

    public int[] getBoard(){
        return this.board;
    }

    public int[][] getStanding_crates(){
        return this.standing_crates;
    }

    public int[][] getToppled_crates(){
        return this.toppled_crates;
    }





}
