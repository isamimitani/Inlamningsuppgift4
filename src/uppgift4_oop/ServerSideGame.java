package uppgift4_oop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class ServerSideGame {
    
    private String question;
    private String [] alternatives;
    private int answer;
    private int score;
    ServerSidePlayer currentPlayer;
    
    public ServerSideGame(String question, String []alternatives, int answer, int score){
        this.question = question;
        this.alternatives = alternatives;
        this.answer = answer;
        this.score = score;
    }
    
    public String getQuestion(){
        return question;
    }
    
    public String [] getAlternatives(){
        return alternatives;
    }
    public int answer(){
        return answer;
    }
    public int getScore(){
        return score;
    }
    
    public String toString(){
        String print = question + "\n";
        for(String alternative: alternatives){
            print += alternative + "\n";
        }
        print += "Answer: " + answer + "\n";
        return print;
    }
    
}
//class Questions{
//    private ArrayList<ServerSideGame> questions = new ArrayList<>();
//    
//    public Questions() throws FileNotFoundException{
//        try{
//            FileReader file = new FileReader("questions.txt");
//            BufferedReader reader = new BufferedReader(file);
//            Scanner scan = new Scanner(reader);
//            Scanner keyBoardInput = new Scanner(System.in);
//            
//            String line;
//            String question = "";
//            String [] alternatives = null;
//            
//            
//            int answer = 0;
//            
//            int numberOfAlternatives = 0;
//            int counter = 0;
//            int score = 0;
//            
////            for (int i= 0; i<questions.size(); i++)
////            {
////                System.out.println(questions.size());
////                String answer = keyBoardInput.nextLine();
////                //compareing answers to actual answer to question
////                if(answer.equals(questions.)){
////                    score++;
////                }
//                
//            }
//            
//            do{ 
//                
//                do{
//                    line = scan.nextLine();
//                    
//                    if(line.contains("?")){//stores the question
//                        question = line;
//                    }
//                    else if( counter ==0 && line.length()==1){//stores the number of alternatives
//                        numberOfAlternatives = Integer.valueOf(line);
//                        alternatives = new String [numberOfAlternatives];
//                    }
//                    else if (line.contains(")")){ //stores the alternatives
//                        alternatives [counter++]= line;
//                    }
//                    else if (Character.isDigit(line.indexOf(0)) || counter == numberOfAlternatives){//stores the answer
//                        answer = Integer.valueOf(line);
//                    }
//                    
//                }
//                        
//                    while (answer == 0);
//            }
// 
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        
//    }
//}
