package cn.j.netstorage.tool;

import cn.j.netstorage.Entity.Aria2Entity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Aria2Http {

    public static String post(String uri, String jsonStr) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

//
//    public Aria2RPCWebSocket(String url) throws URISyntaxException {
//        super(new URI(url));
//    }
//
//    @Override
//    public void onOpen(ServerHandshake shake) {
//        System.out.println("已经连接...");
//    }
//
//    @Override
//    public void onMessage(String s) {
//        System.out.println(s);
//    }
//
//    @Override
//    public void onClose(int i, String s, boolean b) {
//
//    }
//
//    @Override
//    public void onError(Exception e) {
//
//    }
//
//    public static void dowload(Aria2RPCWebSocket client, String url, String filePath) {
//        String text = Aria2RPCWebSocket.createJsonRPC(
//                "download-1",
//                "aria2.addUri",
//                new Object[]{url}, Collections.singletonMap("dir", filePath));
//        client.send(text);
//    }
//
//    private static Aria2RPCWebSocket client;
//
//    public static Aria2RPCWebSocket getClient() throws URISyntaxException {
//        if (client==null){
//            client = new Aria2RPCWebSocket("ws://127.0.0.1:6800/jsonrpc");
//            System.out.println(client.getReadyState());
//
//            // 连接 WebSocket 服务器
//            client.connect();
//            // 当服务器连接上时，发送 Json 数据
//            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//                System.out.println("等待连接...");
//            }
//        }
//        return client;
//    }
//
//
//    public static String getDownloadList(Aria2RPCWebSocket client,String id) {
//       Object[] object= new Object[]{Arrays.asList("gid",
//                "totalLength",
//                "completedLength",
//                "uploadSpeed",
//                "downloadSpeed",
//                "connections",
//                "numSeeders",
//                "seeder",
//                "status",
//                "errorCode",
//                "verifiedLength",
//                "verifyIntegrityPending",
//                "files",
//                "bittorrent",
//                "infoHash")};
//       String searchStatment= createJsonRPC(id
//        ,"aria2.tellActive",object);
//       client.send(searchStatment);
//        return null;
//    }
//
//    public static String createJsonRPC(String id,String method,Object ... obj) {
//        Aria2Entity aria2Entity = new Aria2Entity();
//        aria2Entity.setId(id);
//        aria2Entity.setMethod(method);
//        aria2Entity.setVersion("2.0");
//        aria2Entity.setParams(obj);
//        return new Gson().toJson(aria2Entity);
//    }

}