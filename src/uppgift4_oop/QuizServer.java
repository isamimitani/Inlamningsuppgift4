package uppgift4_oop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author isami
 */
public class QuizServer {
    
    int portNumber = 12540;
    
    public QuizServer(){
        try(
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            String inputline;
            String outputline;
            
            System.out.println(clientSocket.getInetAddress().getHostAddress());
            System.out.println(clientSocket.getInetAddress().getCanonicalHostName());
            System.out.println(clientSocket.getPort());
            
            out.println("Write your text to repeat: ");
            
            while((inputline = in.readLine()) != null){
                out.println(inputline);
            }
              
        } catch(IOException ioe){
            System.exit(0);
        }
        
    }
    
    public static void main(String[] args){
        new QuizServer();
    }
    
}
