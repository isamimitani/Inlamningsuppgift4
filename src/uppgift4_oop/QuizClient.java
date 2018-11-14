package uppgift4_oop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author isami
 */
public class QuizClient {
    String hostName = "127.0.0.1"; //localhost
    int portNumber = 12540;
    
    public QuizClient(){
        try(
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            Scanner sc = new Scanner(System.in);
        ){
            String fromServer;
            String fromUser;
            
            while((fromServer = in.readLine()) != null){
                System.out.println("From Server: " + fromServer);
                
                fromUser = sc.nextLine();
                if(fromUser != null || fromUser.equals("")){
                    out.println(fromUser.trim());
                }
                        
            }
         
        } catch(IOException ioe){
            System.exit(0);
        }
    }
    
    public static void main(String[] args){
        QuizClient q = new QuizClient();
    }
}
