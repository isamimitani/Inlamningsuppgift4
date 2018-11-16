package uppgift4_oop;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author 
 */
public class ServerSideGame {
    

    private ServerSidePlayer currentPlayer; 
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Quiz> questions = new ArrayList<>();
    
    public ServerSideGame(){

    }
    
//    public void createCategories(){
//        for(int i=0; i<questions.size(); i++){
//            String s = questions.get(i).getCategory();
//            if(!categories.contains(s)){
//                categories.add(s);
//            }
//        }
//    }
//    
//    public Quiz getRandomQuiz(){
//        Quiz quiz = null;
//        Random rand = new Random();
//        quiz = list.get(rand.nextInt(questions.size()));
//        return quiz;
//    }
//    
//    public Quiz getRandomQuiz(String category){
//        Quiz quiz = null;
//        ArrayList<Quiz> list = new ArrayList<>();
//        for(Quiz q : questions){
//            if(q.getCategory().equals(category)){
//                list.add(q);
//            }
//        }
//        Random rand = new Random();
//        quiz = list.get(rand.nextInt(list.size()));
//        return quiz;
//    }
    
    public List<String> getCategories(){
        return categories;
    }
    
    public void setCurrentPlayer(ServerSidePlayer player){
        currentPlayer = player;
    }
    
    public String toString(){
        return "Question";
    }   
}
