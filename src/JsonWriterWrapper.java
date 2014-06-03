import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by abhisheksen on 4/23/14.
 */
public class JsonWriterWrapper {

    private JsonWriter writer;

    public JsonWriterWrapper(String fileName) throws IOException{

        writer = new JsonWriter(new FileWriter(fileName));

    }

    public JsonWriter getWriter(){

        return this.writer;

    }






}
