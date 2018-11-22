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
            
//            Object fromServer;
//            
//            while((fromServer = in.readObject()) != null){
//                System.out.println(fromServer);
//                if(fromServer.toString().equals("Question")){
//                    Quiz q = (Quiz)fromServer;
//                    System.out.println(q.getCategory() + ":" + q.getQuizText());
//                    System.out.println(q.getAlt1() + "," + q.getAlt2() +","+
//                            q.getAlt3() + "," + q.getAlt4());
//                    out.writeObject("ANSWERED");
//                }
//            }
         
        } catch(IOException ex){
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
        }
//        } finally {
//            try {
//                in.close();
//                out.close();
//                socket.close();
//            } catch (IOException ex) {
//                Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
//            }          
//        }
    }
    
    public static void main(String[] args){
        QuizClient q = new QuizClient();
        q.play();
    }
    
    public void play(){
        
        Object fromServer;
  
        try {
            while((fromServer = in.readObject()) != null){
                System.out.println(fromServer);
                switch(fromServer.toString()){
                    case "WELCOME":
                        //lagra antal ronder och antal frågor  i sin variabler
                        //initiera int[][] med antal ronder och antal frågar
                        //anpassa GUI efter antal ronder och antal frågor
                        break;
                    case "QUESTION":
                        Quiz q = (Quiz)fromServer;
                        System.out.println(q.getCategory() + ":" + q.getQuizText());
//                        System.out.println(q.getAlt1() + "," + q.getAlt2() +","+
//                            q.getAlt3() + "," + q.getAlt4());
                        out.writeObject("ANSWERED 0");
                        break;
                    case "MESSAGE":
                        // visa meddelande i en label i väntläge?
                        System.out.println("Got a message.");
                        break;
                    case "END_OF_ROUND":
                        //byta panel till väntläge visa att man måste vänta tills opponenten svarar
                        System.out.println("End of round. Must wait!");
                        break;
                    case "YOUR_TURN":
                        //visa "start" knapp på väntläge genom att synliggöra knappen på panelen
                        System.out.println("My turn!");
                        break;
                    case "END_OF_GAME":
                        System.out.println("END!!!!");
                        //gemför resultatet och visa vem som vann eller förlorade
                        break;
                    case "RESULT":
                        //tar emot int[][] array som är opponentens resultat och lagra den som opponentens resultat
                        break;      
                }
            }
        } catch (IOException ex) {
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
}
