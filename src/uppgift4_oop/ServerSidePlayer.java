package uppgift4_oop;

import java.net.Socket;

/**
 *
 * @author 
 */
public class ServerSidePlayer extends Thread{
 
    Socket socket;
    ServerSidePlayer opponent;
    ServerSideGame game;
    
    public ServerSidePlayer(Socket socket, ServerSideGame game){
        this.socket = socket;
        this.game = game;
        
        System.out.println("New Connection:");
        System.out.println("IP-Address=" + socket.getInetAddress().getHostAddress());
        System.out.println("Port=" + socket.getPort() + "\n");
    }
    
    /**
    * Accepts notification of who the opponent is.
    */
    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }
    
    /**
    * The run method of this thread.
    */
    @Override
    public void run(){
        
    }
    
}
