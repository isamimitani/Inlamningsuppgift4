package uppgift4_oop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;



/**
 * Klass för spelet
 * @author 
 */
public class ServerSideGame {
    
    private static Properties prop = new Properties();
    private static final String PATH = "src//uppgift4_oop//game.properties";

    private ServerSidePlayer currentPlayer; 
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Quiz> questions = new ArrayList<>();   
    private ArrayList<Quiz> sendList = new ArrayList<>();
    private String currentCategory;
    
    private int numOfRounds;
    private int numOfQuestions;
    public int roundCounter = 0;
    public int questionCounter = 0;
    
    //Initierar Properties med property-filen
    static{
        try{
            prop.load(new FileInputStream(PATH));
            System.out.println("Number of Rounds: " + prop.getProperty("numberOfRounds"));
            System.out.println("Number of Questions: " + prop.getProperty("numberOfQuestions"));
            System.out.println("File path of Questions: " + prop.getProperty("quizFilePath"));
            
        } catch(IOException ex){
            System.out.println("IO Exception.");
        }
    }
    
    // Konstruktor
    public ServerSideGame(){
        initQuestions();
        initCategories();
        currentCategory = getRandomCategory();
        numOfRounds = Integer.parseInt(prop.getProperty("numberOfRounds"));
        numOfQuestions = Integer.parseInt(prop.getProperty("numberOfQuestions"));     
    }
    
    // Läser in fil för frågor och lagrar dem i en lista
    private void initQuestions(){
        try(Scanner sc = new Scanner(new File(prop.getProperty("quizFilePath")))){
            while(sc.hasNextLine()){
                String[] args = sc.nextLine().split(",");
                Quiz quiz = new Quiz(args[0].trim(), args[1].trim(), args[2].trim(), args[3].trim(),
                                    args[4].trim(), args[5].trim(), args[6].trim());
                questions.add(quiz);
            }
        } catch(IOException ex){
            System.out.println("File not found.");
        } 
    }
    
    // Tar upp olika kategorier från frågelista och lagrar dem i en lista
    public void initCategories(){
        for(int i=0; i<questions.size(); i++){
            String s = questions.get(i).getCategory();
            if(!categories.contains(s)){
                categories.add(s);
            }
        }
    }
    
    // Retunerar en slumpmässig kategori från kategorilista
    public String getRandomCategory(){
        String category = null;
        Random rand = new Random();
        category = categories.get(rand.nextInt(categories.size()));
        return category;
    }
 
    // Addar en slumpmässig fråga med viss kategori till sendlist
    public void setRandomQuiz(String category){
        Quiz quiz = null;
        ArrayList<Quiz> list = new ArrayList<>();
        for(Quiz q : questions){
            if(q.getCategory().equals(category)){
                list.add(q);
            }
        }
        Random rand = new Random();
        quiz = list.get(rand.nextInt(list.size()));
        sendList.add(quiz);
    }
    
    // Returnerar en fråga på position i från sendList
    public Quiz getQuiz(int i){
        return sendList.get(i);
    }
    
    // Byter spelare och anropar metoder som skickar resultat av förra spelare
    // och fråga
    public void changePlayer(ServerSidePlayer player){
        int[] array = currentPlayer.result;
        System.out.println("sending opponent result");
        for(int i=0; i<array.length; i++){
            System.out.print(array[i]);
        }
        System.out.println();
        currentPlayer = player;
        currentPlayer.otherPlayerAnswered();
        currentPlayer.sendOpponentResult(array);
        
    }
    
    // Skickar sista spelarens resultat och meddelar att spelet är slut
    public void endGame(ServerSidePlayer player){
        int[] array = currentPlayer.result;
        System.out.println("sending opponent result");
        for(int i=0; i<array.length; i++){
            System.out.print(array[i]);
        }
        System.out.println();
        currentPlayer = player;
        currentPlayer.sendOpponentResult(array);
        player.gameIsOver();        
    }
       
    // Kollar om spelet är slut
    public boolean isEndOfGame(){
        if(roundCounter == numOfRounds*2){
            System.out.println("End Of Game....");
            return true;
        } else {
            return false;
        }
    }
    
    // Kollar om ronden är slut. 
    // Ändrar currentCategory om båda spelarna har spelat klart en rond
    public boolean isEndOfRound(){
        if(questionCounter == numOfQuestions){
            questionCounter = 0;
            roundCounter++;
            if(roundCounter % 2 ==0){
                currentCategory= getRandomCategory();
                System.out.println("Category has changed");
            }
            System.out.println("End of Round..");
            return true;
        } else {
            return false;
        }
    }

    // -- Getters och setters --//
    public String getCurrentCategory(){
         return currentCategory;
    }
    
    public void setCurrentPlayer(ServerSidePlayer player){
        currentPlayer = player;
    }
    
    public int getNumOfRounds() {
        return numOfRounds;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }
    
    
//////Unused method//////      
//    public <T>T getRandom(String s){ 
//        if(s.equalsIgnoreCase("Quiz")){
//            Quiz quiz = null;
//            Random rand = new Random();
//            quiz = questions.get(rand.nextInt(questions.size()));
//            return (T)quiz;
//        } else if(s.equalsIgnoreCase("Category")){
//            String category = null;
//            Random rand = new Random();
//            category = categories.get(rand.nextInt(categories.size()));
//            return (T)category;
//        } else {
//            return null;
//        }
//    }
    
//////Unused method//////      
//    public List getRandomQuizList(String category, int num){
//        Quiz quiz = null;
//        int count = 0;
//        Random rand = new Random();
//        ArrayList<Quiz> list = new ArrayList<>();
//        while(list.size()!=num){
//            quiz = questions.get(rand.nextInt(questions.size()));
//            if(quiz.getCategory().equals(category)){
//                list.add(quiz);
//            }
//        }
//        return list;
//    }
    
     
}
