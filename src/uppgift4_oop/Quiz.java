package uppgift4_oop;

import java.io.Serializable;

/**
 *
 * @author 
 */
public class Quiz implements Serializable{
    
    private static final long serialVersionUID = 42L;
    
    private String category;
    private String quizText;
    private String answer;
    private String alt1;
    private String alt2;
    private String alt3;
    private String alt4;

    public Quiz(String category, String quizText, String answer, String alt1, 
            String alt2, String alt3, String alt4) {
        this.category = category;
        this.quizText = quizText;
        this.answer = answer;
        this.alt1 = alt1;
        this.alt2 = alt2;
        this.alt3 = alt3;
        this.alt4 = alt4;
    }

    public String getCategory() {
        return category;
    }

    public String getQuizText() {
        return quizText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAlt1() {
        return alt1;
    }

    public String getAlt2() {
        return alt2;
    }

    public String getAlt3() {
        return alt3;
    }

    public String getAlt4() {
        return alt4;
    }

    @Override
    public String toString(){
        return "QUESTION";
    }

}
