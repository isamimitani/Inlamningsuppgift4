package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klass för en spelar på serversidan
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
    int[] result;
    
    // Konstruktor
    public ServerSidePlayer(Socket socket, ServerSideGame game, int mark){
        this.socket = socket;
        this.game = game;
        this.mark = mark;
        
        result = new int[game.getNumOfRounds() * game.getNumOfQuestions()];
        
        System.out.println("New Connection:");
        System.out.println("IP-Address=" + socket.getInetAddress().getHostAddress() + 
                " Port=" + socket.getPort() + "\n");
        
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("WELCOME " + game.getNumOfRounds() + " " + game.getNumOfQuestions());
            out.writeObject("MESSAGE Waiting for opponent to connect");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    
    // Setter för opponent
    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }
    
    // Meddelar clienten att det är hens tur och skickar en fråga
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
    
    // Skickar opponentens resultat till clienten
    public void sendOpponentResult(int[] res){
        try {
            out.writeObject("RESULT");
            System.out.println("method:sendOpponentResult");
            for(int i=0; i<res.length; i++){
                System.out.print(res[i]);
            }
            // Måste göra reset annars skickar man gammal array hela tiden
            out.reset();
            out.writeObject(res);
        } catch (IOException ex) {
            Logger.getLogger(ServerSidePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Meddelar clienten att spelet är slut
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
                    System.out.println("GOT_ANSWER: " + fromClient.toString());
                    int point = Character.getNumericValue(fromClient.toString().charAt(9));
                    result[questioncounter-1] = point;
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
