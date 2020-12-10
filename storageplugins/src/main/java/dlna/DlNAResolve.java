package dlna;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import socket.Resolve;

import java.util.HashMap;

public class DlNAResolve implements Resolve{

    @Override
    public HashMap<String, Object> Resolve(String text) {
        System.out.println(text);
        JsonObject jsonObject = new Gson().fromJson(text, JsonObject.class);
        jsonObject = jsonObject.getAsJsonObject("params");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uri",jsonObject.get("uri").getAsString());
        hashMap.put("info",jsonObject.get("info").getAsString());
        return hashMap;
    }
}
