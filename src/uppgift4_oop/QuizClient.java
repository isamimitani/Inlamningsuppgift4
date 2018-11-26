package uppgift4_oop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
                        System.out.println(numberOfRounds +" " + numberOfQuestions);
                        //initiera int[] med antal ronder och antal frågar
                        myResult = new int[numberOfRounds*numberOfQuestions];
                        //opponentResult = new int[numberOfRounds*numberOfQuestions];
                        
                        // Must do this, otherwise the program does not work 
                        SwingUtilities.invokeLater(() -> {
                            gui.setPointBox();
                            gui.createResultPanel();
                        });
                        
                        //anpassa GUI efter antal ronder och antal frågor
                        fromServer = null;
                        break;
                    case "QUESTION":
                        currentQuiz = (Quiz)fromServer;
                        
                        // Must do this, otherwise the program does not work 
                        SwingUtilities.invokeLater(() -> {
                            gui.setQuiz(currentQuiz);
                            gui.revalidate();
                        });
                                          
                        System.out.println(currentQuiz.getCategory() + ":" + currentQuiz.getQuizText());
                        fromServer = null;
                        break;
                    case "MESSAGE":
                        // visa meddelande i en label i väntläge?
                        System.out.println("Got a message.");
                        fromServer = null;
                        break;
                    case "END_OF_ROUND":
                        //**TODO**byta panel till väntläge visa att man måste vänta tills opponenten svarar
                        System.out.println("End of round. Must wait!");
                        fromServer = null;
                        break;
                    case "YOUR_TURN":
                        //**TODO**: visa "start" knapp på väntläge genom att synliggöra knappen på panelen
                        System.out.println("My turn! " + fromServer.toString());
                        SwingUtilities.invokeLater(() -> {
                            gui.showStartButton();
                        });
                        fromServer = null;
                        break;
                    case "END_OF_GAME":                        
                        System.out.println("END!!!!");
                        for(int i: myResult)
                            System.out.print(i);
                        System.out.println();
                        //gemför resultatet och visa vem som vann eller förlorade
                        isWinner();     
                        fromServer = null;
                        break;
                    case "RESULT":
                        //tar emot int[] array som är opponentens resultat och lagra den som opponentens resultat
                        fromServer = in.readObject(); 
                        opponentResult =(int[])fromServer;
                        //opponentResult2=null;
                        //opponentResult2 = (List<Integer>)fromServer;
                        System.out.println("Got opponent result");
                        for(int i=0; i<opponentResult.length; i++){
                            System.out.print(opponentResult[i]);
                        }
//                        for(int i=0; i<opponentResult2.size(); i++){
//                            System.out.print(opponentResult2.get(i));
//                        }
                        System.out.println();
                        fromServer = null;
                        break; 
                    default:
                        System.out.println(fromServer);
                        fromServer = null;
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

