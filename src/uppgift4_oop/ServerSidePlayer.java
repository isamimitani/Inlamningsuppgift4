package uppgift4_oop;

import java.net.Socket;

/**
 *
 * @author isami
 */
public class ServerSidePlayer {
 
    Socket socket;
    
    public ServerSidePlayer(Socket socket){
        this.socket = socket;
        System.out.println(socket.getInetAddress().getHostAddress());
        System.out.println(socket.getInetAddress().getCanonicalHostName());
        System.out.println(socket.getPort());
    }
    
}
