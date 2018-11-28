package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.SwingUtilities;

/**
 * KLass för en client(en spelare)
 * @author
 */
public class QuizClient {
    
    private String serverAddress = "127.0.0.1"; //localhost
    private int portNumber = 12540;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private GUI gui;
    public Quiz currentQuiz;
    
    private int[] myResult;
    public int[] opponentResult;
    public int questioncounter = 0;
    public int roundcounter = 0;
    public int mark;
    
    public int numberOfRounds;
    public int numberOfQuestions;
    
    // Konstruktor
    public QuizClient(){
                
        try{
            socket = new Socket(serverAddress, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream()); 
            gui = new GUI(this);
         
        } catch(IOException ex){
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    // Tar emot en sträng och returnerar första ordet
    private String getFirstWord(String str){
        String res;
        int index = str.indexOf(" ");
        if(index >=0){
            res = str.substring(0, index);
        } else {
            res = str;
        }
        return res;
    }
    
    // Kollar om svaret spelaren valde är rätt
    public boolean isRightAnswer(String answer){
        return currentQuiz.getAnswer().equals(answer);
    }
    
    // Sparar spelarens resultat. Om det är spelare1 och det är slutet av en rond
    // visar upp total poäng för ronden på resultatpanelen
    public void saveAnswer(boolean correct){
        if(correct){
            myResult[questioncounter++] = 1;
        } else {
            myResult[questioncounter++] = 0;
        }
        
        if(questioncounter % numberOfQuestions == 0){
            int temp = 0;
            for(int i=roundcounter*numberOfQuestions; 
                    i<roundcounter*numberOfQuestions+numberOfQuestions; i++){
                temp += myResult[i];
            }
            final int temp2 = temp;
            SwingUtilities.invokeLater(() -> {
                gui.setMyPoints(temp2);
            });        
            roundcounter++;
        }
        
    }
    
    // Returnerar summa av en int_arrays varje element
    public int getTotalPoints(int[] array){
        return IntStream.of(array).sum();
    }
    
    // Skickar svaret till Serversidan
    public void sendAnswer(boolean correct){
       try{
            if(correct){
                out.writeObject("ANSWERED " + 1);
            } else {
                out.writeObject("ANSWERED " + 0);
            }
       } catch (IOException ex) {
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Kollar om spelaren har vunnit eller inte
    private void isWinner(){
        int mySum = getTotalPoints(myResult);
        int opponentSum = getTotalPoints(opponentResult);
        System.out.println("my result " + mySum);
        System.out.println("opponent result " + opponentSum);
        if(mySum > opponentSum){
            System.out.println("You won!");
            SwingUtilities.invokeLater(() -> {
                gui.setMessage("You won!");
            });
        } else if(mySum < opponentSum){
            System.out.println("You lost!");
            SwingUtilities.invokeLater(() -> {
                gui.setMessage("You lost!");
            });
        } else {
            System.out.println("Draw!");
            SwingUtilities.invokeLater(() -> {
                gui.setMessage("Draw!");
            });
        }
    }
    
    // Startar loopen för att lyssna på servern
    public void play(){
        
        Object fromServer;
  
        try {
            while((fromServer = in.readObject()) != null){
                System.out.println(fromServer);              
                switch(getFirstWord(fromServer.toString())){
                    case "WELCOME":
                        numberOfRounds = Character.getNumericValue(fromServer.toString().charAt(8));
                        numberOfQuestions = Character.getNumericValue(fromServer.toString().charAt(10));
                        myResult = new int[numberOfRounds*numberOfQuestions];
                        
                        // Must do this, otherwise the program does not work 
                        SwingUtilities.invokeLater(() -> {
                            gui.setPointBox();
                            gui.createResultPanel();
                        });
                        break;
                    case "QUESTION":
                        currentQuiz = (Quiz)fromServer;
                        
                        SwingUtilities.invokeLater(() -> {
                            gui.setQuiz(currentQuiz);
                        });
                                          
                        System.out.println(currentQuiz.getCategory() + ":" + currentQuiz.getQuizText());
                        break;
                    case "MESSAGE":
                        System.out.println("Got a message.");
                        final String message = fromServer.toString();
                        SwingUtilities.invokeLater(() -> {
                            gui.setMessage(message);
                        });
                        break;
                    case "END_OF_ROUND":
                        System.out.println("End of round. Must wait!");
                        SwingUtilities.invokeLater(() -> {
                            gui.setMessage("Opponent's turn. You must wait..");
                        });
                        break;
                    case "YOUR_TURN":
                        mark = Character.getNumericValue(fromServer.toString().charAt(10));
                        System.out.println("My turn! " + fromServer.toString());
                        SwingUtilities.invokeLater(() -> {
                            gui.setMessage("Your turn.");
                            gui.showStartButton();
                        });
                        break;
                    case "END_OF_GAME":                        
                        System.out.println("END!!!!");
                        SwingUtilities.invokeLater(() -> {
                            gui.setMessage("Game is over!");
                            gui.setMyPoints(getTotalPoints(myResult));
                            if(mark==0){
                                gui.setOpponentPoints(getTotalPoints(opponentResult));
                            }
                        });
                        isWinner();     
                        break;
                    case "RESULT":
                        fromServer = in.readObject(); 
                        opponentResult =(int[])fromServer;
                        System.out.println("Got opponent result");
                        for(int i=0; i<opponentResult.length; i++){
                            System.out.print(opponentResult[i]);
                        }
                        System.out.println();
                        if(mark == 0){
                            int temp = 0;
                            System.out.println("RESULT roundcounter " + roundcounter);
                            for(int i=(roundcounter-1)*numberOfQuestions; 
                                    i<(roundcounter-1)*numberOfQuestions+2; i++){
                                temp += opponentResult[i];
                            }
                            final int temp2 = temp;
                            SwingUtilities.invokeLater(() -> {
                                gui.setOpponentPoints(temp2);
                            });
                            System.out.println();
                        }
                        break; 
                    default:
                        System.out.println(fromServer);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
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
        q.play();
    }
}

