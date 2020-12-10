package socket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.util.HashMap;

public class Request {

    private HashMap<String,Object> header=new HashMap<>();
    private HashMap<String,Object> params;
    private JsonObject jsonObject;
    private String text;

    public String getUri(){
        if (params!=null&&params.containsKey("uri")) return params.get("uri").toString();
        return null;
    }

    public String getINFO(){
        if (params!=null&&params.containsKey("info")) return params.get("info").toString();
        return null;
    }

    public String getType(){
        return header.containsKey("Type")?header.get("Type").toString():null;
    }

    public Request(String text){
        this.text=text;
        jsonObject=new Gson().fromJson(text,JsonObject.class);
        header.put("Type",jsonObject.get("Type").getAsString());
    }

    public Request parse(Resolve resolve){
        params=resolve.Resolve(text);
        return this;
    }

}
