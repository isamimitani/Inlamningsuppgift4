package uppgift4_oop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * This class will read and get information from the questions.txt file
 *
 * @author tytrainor
 */
public class Questions {


public class File {

private String filename = "questions.txt";
     
    public String readFile(String filename) throws IOException
	{
	    StringBuilder sb;
	    try(BufferedReader br = new BufferedReader(new FileReader(filename)))
	    {
		sb = new StringBuilder();
		String line = br.readLine();

		while (line != null)
		{
		    sb.append(line);
		    sb.append(System.lineSeparator());
		    line = br.readLine();
		}
            }
		
		return sb.toString();
	}
	
	public String GetFilename()
	{
	     return this.filename;
	}

}
    
}
