
import socket.SocketService;

public class Main{
    public static void main(String[] args) {
        new SocketService(
                args.length==0?
                        6888:Integer.parseInt(args[0])
        );
    }
}
