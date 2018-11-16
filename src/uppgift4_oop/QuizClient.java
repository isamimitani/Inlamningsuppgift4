package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isami
 */
public class QuizClient {
    
    private String serverAddress = "127.0.0.1"; //localhost
    private int portNumber = 12540;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public QuizClient(){
        
        
        try{
            socket = new Socket(serverAddress, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream()); 
            
            Object fromServer;
            
            while((fromServer = in.readObject()) != null){
                System.out.println(fromServer);
                //out.writeObject("ANSWER");
            }
         
        } catch(IOException ex){
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
    }
    
    public static void main(String[] args){
        QuizClient q = new QuizClient();
    }
}
