package uppgift4_oop;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author 
 */
public class ServerSideGame {
    
    private ServerSidePlayer currentPlayer; 
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Quiz> questions = new ArrayList<>();
    
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
}
