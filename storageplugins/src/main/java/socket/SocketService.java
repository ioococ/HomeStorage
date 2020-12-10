package socket;

import dlna.DLNAService;
import dlna.DlNAResolve;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

public class SocketService {
    public SocketService(int port) {
        this.StartSocketServer(port);
    }

    public void StartSocketServer(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket client = socket.accept();
                new RunOnOtherThread(client);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class RunOnOtherThread implements Runnable {
        private Socket socket;

        public RunOnOtherThread(Socket socket) {
            this.socket = socket;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                StringBuilder sb=new StringBuilder();
                String str;
                while((str=input.readLine())!=null){
                    sb.append(str);
                }
                Request request=new Request(sb.toString());
                String type=request.getType();
                System.out.println(type);
                if ("DLNA".equals(type)){
                    request = request.parse(new DlNAResolve());
                    new DLNAService(request.getINFO(),request.getUri()).searchAndConn();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
