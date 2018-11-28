package uppgift4_oop;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.WEST;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Bazgir
 */
class GUI extends JFrame {
    
    QuizClient client;
    
    JFrame jrf = new JFrame("Quizkampen");
    JPanel points = new JPanel();
    JPanel questions = new JPanel(); 
    JPanel answers = new JPanel(); 
    JPanel nextpanel = new JPanel();
    
    JPanel top = new JPanel();
    JPanel main = new JPanel();
   
    JButton next = new JButton("next");
    JButton toResult = new JButton("result");
    JButton start = new JButton("start");
    JLabel message = new JLabel("");
    
    TitledBorder title; 
    Svaren spel = new Svaren(); 
    JLabel text;
    JTextArea question;
    
    boolean currentAnswer = false;
//    int[] myResult;
//    int[] opponentResult;
    
    final String pointsNull = "src//images//pointsNull.png"; 
    final String pointsTrue = "src//images//pointsTrue.png"; 
    final String pointsFalse = "src//images//pointsFalse.png";  
    
    JPanel spelare = new JPanel();
    JPanel score = new JPanel();
    JPanel myScore = new JPanel();
    JPanel opponentScore = new JPanel();
    JPanel rounds = new JPanel();
    JPanel resultat = new JPanel();
    JPanel messagepanel = new JPanel();
    JPanel startpanel = new JPanel();
    JPanel resultatMain = new JPanel();
    
    public GUI(QuizClient client) throws IOException
    { 
        this.client = client;
        
        createMainPanel();

        BufferedImage background = ImageIO.read(new File("src//images//background.jpg")); 
        jrf.setContentPane(new ImagePanel(background));
        
        //jrf.add(main);  
        //jrf.add(resultatMain);
        jrf.pack(); 
        jrf.setLocation(100, 100); 
        jrf.setSize(400,600);
        jrf.setResizable(false);
        jrf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //jrf.setVisible(true);
        jrf.setVisible(false);
    }
    
    public void createMainPanel(){
        Font omgång = new Font("Ariel",Font.BOLD, 12);
        Font fråga = new Font("Ariel",Font.ITALIC, 14);
        //Main panel för frågespel
        top.setLayout(new BorderLayout());
        top.setBackground(Color.decode("#317AD5"));  
        
        text = new JLabel("Runda x mot Spelare X  ");  
        text.setForeground(Color.WHITE);
        text.setFont(omgång);
        points.setBackground(Color.decode("#317AD5"));
        top.add(points, WEST); 
        top.add(text, EAST);
                
        title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Kategori");   
        title.setTitleJustification(TitledBorder.CENTER); 
       
        question = new JTextArea(12,32);  //**TODO**Ändra JTextArea till JTextPane  KOLLA PÅ NÄTET OM DU INTE FÖRSTÅR
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setText("Sample text"); 
//        question.setAlignment(Component.CENTER);
        question.setFont(fråga); 
        question.setEditable(false); 
        question.setBorder(title); 
        
        questions.setBackground(Color.decode("#317AD5"));
        answers.setBackground(Color.decode("#317AD5"));
        questions.add(question); 
        answers.add(spel);
        
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));  
        main.add(top);
        main.add(questions); 
        main.add(answers); 
        
        // next button and its action
        nextpanel.setBackground(Color.decode("#317AD5"));
        nextpanel.add(next);
        nextpanel.add(toResult);
        main.add(nextpanel);
        next.setVisible(false);
        toResult.setVisible(false);
        next.addActionListener(ae -> {
            client.currentQuiz = null;
            client.saveAnswer(currentAnswer);
            client.sendAnswer(currentAnswer);
            if(client.currentQuiz != null){
                spel.game();
            } else {
                next.setVisible(false);
                toResult.setVisible(true);
                jrf.revalidate();
            }
        });
        toResult.addActionListener(ae->{
            jrf.setVisible(false);
            
            if(client.mark == 1){
                int temp = 0;
                System.out.println("toResult roundcounter " +client.roundcounter);
                for(int i=(client.roundcounter-1)*client.numberOfQuestions; 
                    i<(client.roundcounter-1)*client.numberOfQuestions+client.numberOfQuestions; i++){
                    temp += client.opponentResult[i];
                }
                setOpponentPoints(temp);
                if(client.roundcounter == client.numberOfRounds){
                    setOpponentPoints(client.getTotalPoints(client.opponentResult));
                }
            }
            
            jrf.remove(main);
            jrf.add(resultatMain);
            start.setVisible(false);
            jrf.setVisible(true);
            jrf.revalidate();
        });
    }
    
    public void createResultPanel(){
        //Resultat panel för resultat för score samt tidigare rundor etc. 
        Font players = new Font("",Font.BOLD,16);
        Font round = new Font("",Font.BOLD,15);
        Font big = new Font("",Font.BOLD, 30);

        spelare.setLayout(new BorderLayout()); 
        spelare.setBackground(Color.decode("#518FDB"));
        JLabel spelareA = new JLabel("YOU");  
        JLabel spelareB = new JLabel("OPPONENT");  
        spelareA.setFont(players);
        spelareB.setFont(players);
        spelare.add(spelareA, WEST); 
        spelare.add(spelareB, EAST); 

//        score.setLayout(new GridLayout(client.numberOfRounds,1,20,20));
//        for(int z = 1; z <= client.numberOfRounds; z++){                //Ändra värdet på 4:an till det variabel som motsvarar score.
//            JLabel scoreA = new JLabel("-", SwingConstants.CENTER); 
//            JLabel rundaX = new JLabel(" Runda " + z, SwingConstants.CENTER); 
//            JLabel scoreB = new JLabel("-" , SwingConstants.CENTER);  
//            scoreA.setFont(rounds);
//            rundaX.setFont(rounds);
//            scoreB.setFont(rounds);
//            score.setBackground(Color.decode("#518FDB"));
//            score.add(scoreA); 
//            score.add(rundaX);  
//            score.add(scoreB); 
//        }  
        score.setLayout(new GridLayout(1,client.numberOfRounds,20,20));
        rounds.setLayout(new GridLayout(client.numberOfRounds+1,1, 20, 20));
        myScore.setLayout(new GridLayout(client.numberOfRounds+1,1, 20, 20));
        opponentScore.setLayout(new GridLayout(client.numberOfRounds+1,1, 20, 20));
        for(int z = 1; z <= client.numberOfRounds; z++){                //Ändra värdet på 4:an till det variabel som motsvarar score.
            JLabel rundaX = new JLabel("Runda " + z, SwingConstants.CENTER); 
            rundaX.setFont(round);
            rounds.add(rundaX);
        }
        JLabel total = new JLabel("Total", SwingConstants.CENTER); 
        total.setFont(round);
        rounds.add(total);
        myScore.setBackground(Color.decode("#518FDB"));
        rounds.setBackground(Color.decode("#518FDB"));
        opponentScore.setBackground(Color.decode("#518FDB"));
        score.setBackground(Color.decode("#518FDB"));
        score.add(myScore);
        score.add(rounds);
        score.add(opponentScore);
//        JLabel scoreCurrentA = new JLabel("x", SwingConstants.CENTER);
//        JLabel between = new JLabel(" Total result shows here ", SwingConstants.CENTER);
//        JLabel scoreCurrentB = new JLabel("x", SwingConstants.CENTER); 
//        scoreCurrentA.setFont(big); 
//        scoreCurrentB.setFont(big); 
//        resultat.setBackground(Color.decode("#518FDB"));
//        scoreCurrentA.setVisible(true);
//        between.setVisible(true);
//        scoreCurrentB.setVisible(true);
//        resultat.add(scoreCurrentA); 
//        resultat.add(between);
//        resultat.add(scoreCurrentB);
        
        messagepanel.add(message);
        //messagepanel.setBackground(Color.decode("#518FDB"));

        startpanel.setBackground(Color.decode("#518FDB"));
        startpanel.add(start);
        start.setVisible(false);
        start.addActionListener(ae -> {
            jrf.remove(resultatMain);
            jrf.add(main);
            jrf.revalidate();
        });

        resultatMain.setLayout(new BoxLayout(resultatMain, BoxLayout.PAGE_AXIS));  
        resultatMain.add(spelare);
        resultatMain.add(score);  
        //resultatMain.add(resultat); 
        resultatMain.add(messagepanel);
        resultatMain.add(startpanel);
        resultatMain.setSize(300, 150);
        jrf.add(resultatMain);
        jrf.setVisible(true);
    }
    
    public void setMyPoints(int points){
        JLabel point = new JLabel(String.valueOf(points));
        myScore.add(point);
        jrf.revalidate();
    }
    
    public void setOpponentPoints(int points){
        JLabel point = new JLabel(String.valueOf(points));
        opponentScore.add(point);
        jrf.revalidate();        
    }
    
    public void showStartButton(){
        start.setVisible(true);
        jrf.revalidate();
    }
    
    public void setMessage(String text){
        message.setText(text);
        jrf.revalidate();
    }
    
    public void setQuiz(Quiz quiz){

        title.setTitle(quiz.getCategory());
        question.setText(quiz.getQuizText());
        String[] choices = {quiz.getAlt1(), quiz.getAlt2(),
                            quiz.getAlt3(), quiz.getAlt4()};
        for(int i=0; i<choices.length; i++){
            spel.svaren.get(i).setText(choices[i]);
        }

        spel.game();
        jrf.revalidate();
    }
    
    public void setPointBox(){
        //Anpassas efter antal frågor
        int numOfQuestions = client.numberOfQuestions;
        System.out.println("GUI number of questions" + numOfQuestions);
        for (int x = 0; x < numOfQuestions; x++){ 
            JLabel po = new JLabel(new ImageIcon(pointsNull));  
            points.add(po);
        }
        jrf.revalidate();
    }
    
    final class Svaren extends JPanel implements ActionListener{ 
    
        List<JButton> svaren = new ArrayList<>(4);
        JButton svar; 

        public Svaren() throws IOException
        {   
            setBackground(Color.decode("#317AD5"));
            setLayout(new GridLayout(2,2,6,20));
            for(int i=1; i<5; i++){ 
                svar = new JButton(String.valueOf(i));  
                svar.setPreferredSize(new Dimension(180,90)); 
                svar.setForeground(Color.WHITE); 
                svar.setBackground(Color.BLACK); 
                svar.setOpaque(true);
                svaren.add(svar);  
                svar.addActionListener(this); 

               //game();          
            }       
        } 

        public void game(){
            for(int i=0; i<svaren.size();i++){
                this.remove(svaren.get(i));
            }
            Collections.shuffle(svaren);
            svaren.forEach(svar -> {
                svar.setEnabled(true);
                svar.setForeground(Color.WHITE); 
                svar.setBackground(Color.BLACK);
                this.add(svar);
            });
            next.setVisible(false);
            toResult.setVisible(false);
            jrf.revalidate();    
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i=0; i<svaren.size() ; i++){
                if(e.getSource() == svaren.get(i)){
                    if(client.isRightAnswer(svaren.get(i).getText())){
                        svaren.get(i).setBackground(Color.GREEN);
                        currentAnswer = true;
                        //**TODO** poäng box måste ändras
                    } else {
                        svaren.get(i).setBackground(Color.RED);
                        for(int j=0; j< svaren.size(); j++){
                            if(client.isRightAnswer(svaren.get(j).getText())){
                                svaren.get(j).setBackground(Color.GREEN);
                            }
                        }
                        currentAnswer = false;
                        //**TODO** poäng box måste ändras
                    }
                }
            }
            next.setVisible(true);
            for(int i=0; i<svaren.size(); i++){
                svaren.get(i).setEnabled(false);
            }
            this.revalidate();       
        }
    } 

} 

// Panel for the background image
class ImagePanel extends JPanel
{
    private Image image;

    public ImagePanel(Image image) 
    {
        this.image = image; 
    }
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);  
    }
}

