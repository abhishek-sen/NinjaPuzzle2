/**
 * Created by abhisheksen on 4/23/14.
 */
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class GameConfigReader {

    private JsonFormat jsonData;

    public GameConfigReader(String fname) throws IOException{


        JsonReader jsonReader = new JsonReader(new FileReader(fname));

        Gson gson = new Gson();


        this.jsonData = gson.fromJson(jsonReader, JsonFormat.class);



    }

    public JsonFormat getJsonData(){
        return this.jsonData;
    }
}
