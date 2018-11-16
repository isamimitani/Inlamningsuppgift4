package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class ServerSidePlayer extends Thread{
 
    Socket socket;
    ServerSidePlayer opponent;
    ServerSideGame game;
    ObjectOutputStream out;
    ObjectInputStream in;
    int mark;
    
    public ServerSidePlayer(Socket socket, ServerSideGame game, int mark){
        this.socket = socket;
        this.game = game;
        this.mark = mark;
        
        System.out.println("New Connection:");
        System.out.println("IP-Address=" + socket.getInetAddress().getHostAddress() + 
                " Port=" + socket.getPort() + "\n");
        
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("WELCOME");
            out.writeObject("MESSAGE Waiting for opponent to connect");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
     
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
        try {
            out.writeObject("MESSAGE All players connected");
            
            if(mark==0){
                out.writeObject("MESSAGE Your turn");
            }
            
            while(true){
                Object fromClient = in.readObject();
                if(fromClient.toString().startsWith("ANSWER")){
                    out.writeObject("Got answer");
                } 
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
