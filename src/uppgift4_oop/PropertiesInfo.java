
package uppgift4_oop;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 * @author tytrainor
 * Creating a properties class. 
 * Creating FileReader object that passes in values stores in properties file. Exception included.
 */
public class PropertiesInfo {
  private String  numberOfQuestions;
  private String  numberOfROunds;
public static void main (String []args) throws IOException {
    Properties prop = new Properties();
    InputStream input = null;

	try {

		input = new FileInputStream("src/uppgift4_oop/game.properties");

		// load a properties file
		prop.load(input);
                String questionsPerRound = prop.getProperty("numberOfQuestions");
                String roundsPerPerson = prop.getProperty("numberOfROunds");

		// get the property value and print it out
		System.out.println(prop.getProperty("numberOfQuestions"));
		System.out.println(prop.getProperty("numberOfROunds"));
		

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

  }
}