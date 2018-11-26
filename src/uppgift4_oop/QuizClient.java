package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.SwingUtilities;

/**
 *
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
    private int[] opponentResult;
    private List<Integer> opponentResult2;
    private int questioncounter = 0;
    
    public int numberOfRounds;
    public int numberOfQuestions;
    
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
    
    public boolean isRightAnswer(String answer){
        return currentQuiz.getAnswer().equals(answer);
    }
    
    public void saveAnswer(boolean correct){
        if(correct){
            myResult[questioncounter++] = 1;
        } else {
            myResult[questioncounter++] = 0;
        }
    }
    
    public int getTotalPoints(){
        return IntStream.of(myResult).sum();
    }
    
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
    
    private void isWinner(){
        int mySum = IntStream.of(myResult).sum();
        int opponentSum = IntStream.of(opponentResult).sum();
        System.out.println("my result " + mySum);
        System.out.println("opponent result " + opponentSum);
        if(mySum > opponentSum){
            System.out.println("You won!");
        } else if(mySum < opponentSum){
            System.out.println("You lost!");
        } else {
            System.out.println("Draw!");
        }
    }
    
    public void play(){
        
        Object fromServer;
  
        try {
            while((fromServer = in.readObject()) != null){
                System.out.println(fromServer);              
                switch(getFirstWord(fromServer.toString())){
                    case "WELCOME":
                        //lagra antal ronder och antal frågor  i variabler
                        numberOfRounds = Character.getNumericValue(fromServer.toString().charAt(8));
                        numberOfQuestions = Character.getNumericValue(fromServer.toString().charAt(10));
                        
                        //initiera int[] med antal ronder och antal frågar
                        myResult = new int[numberOfRounds*numberOfQuestions];
                        
                        // Must do this, otherwise the program does not work 
                        SwingUtilities.invokeLater(() -> {
                            gui.setPointBox();
                            gui.createResultPanel();
                        });
                        
                        //anpassa GUI efter antal ronder och antal frågor
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
                        });
                        for(int i: myResult)
                            System.out.print(i);
                        System.out.println();
                        //gemför resultatet och visa vem som vann eller förlorade
                        isWinner();     
                        break;
                    case "RESULT":
                        //tar emot int[] array som är opponentens resultat och lagra den som opponentens resultat
                        fromServer = in.readObject(); 
                        opponentResult =(int[])fromServer;
                        System.out.println("Got opponent result");
                        for(int i=0; i<opponentResult.length; i++){
                            System.out.print(opponentResult[i]);
                        }
                        System.out.println();
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

