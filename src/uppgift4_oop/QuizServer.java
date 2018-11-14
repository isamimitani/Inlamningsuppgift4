package uppgift4_oop;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isami
 */
public class QuizServer {
    
    private int portNumber = 12540;
    private ServerSocket serverSocket;
    
    public QuizServer(){
              
        try{
            serverSocket = new ServerSocket(portNumber);
            
            while(true){
                ServerSidePlayer player1 = new ServerSidePlayer(serverSocket.accept());
                ServerSidePlayer player2 = new ServerSidePlayer(serverSocket.accept());
                //player1.setOpponent(player2);
                //player2.setOpponent(player1);
                //player1.start();
                //player2.start();
                
            }
              
        } catch(IOException ioe){
            Logger.getLogger(QuizServer.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(QuizServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void main(String[] args){
        new QuizServer();
    }
    
}
