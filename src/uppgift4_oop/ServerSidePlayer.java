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
    int mark; // håller koll på 0 = player1; 1=player2
    String currentCategory;
    int questioncounter = 0;
    
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
            //**TODO** must send number of rounds and questions. get info from game
            out.writeObject("WELCOME 2 2");
            out.writeObject("MESSAGE Waiting for opponent to connect");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    
    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }
    
    public void otherPlayerAnswered(){
        try {
            currentCategory = game.getCurrentCategory();
            out.writeObject("YOUR_TURN " + mark);
            if(mark == 0){
                game.setRandomQuiz(currentCategory);
                out.writeObject(game.getQuiz(questioncounter++));
            } else {
                out.writeObject(game.getQuiz(questioncounter++));
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendOpponentResult(int[] result){
        try {
            out.writeObject("RESULT");
            out.writeObject(result);
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gameIsOver(){
        try {
            out.writeObject("END_OF_GAME");
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * The run method of this thread.
    */
    @Override
    public void run(){
        try {
            out.writeObject("MESSAGE All players connected");
            
            if(mark==0){
                out.writeObject("YOUR_TURN " + mark);
                currentCategory = game.getCurrentCategory();
                game.setRandomQuiz(currentCategory);
                out.writeObject(game.getQuiz(questioncounter++));
            }
            
            while(true){
                Object fromClient = in.readObject();
                if(fromClient.toString().startsWith("ANSWERED")){
                    //**TODO** måste spara resultatet
                    System.out.println("GOT_ANSWER: " + fromClient.toString());
                    game.questionCounter++;
                    System.out.println("questioncounter: " + game.questionCounter);
                    //If it is end of round
                    if(game.isEndOfRound()){
                        System.out.println("roundcounter: " + game.roundCounter);
                        //checks if the game is not over
                        if(!game.isEndOfGame()){
                            out.writeObject("END_OF_ROUND");
                            game.changePlayer(opponent);
                        } else {    //iIf the game is over
                            //must send the result
                            gameIsOver();
                            game.endGame(opponent);
                        }
                    } else {    //Send one more question
                        if(mark == 0){
                            currentCategory = game.getCurrentCategory();
                            game.setRandomQuiz(currentCategory);   
                            out.writeObject(game.getQuiz(questioncounter++));
                        } else {
                            out.writeObject(game.getQuiz(questioncounter++));
                        }
                    }
                } 
            }
            
        } catch (IOException | ClassNotFoundException ex) {
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
