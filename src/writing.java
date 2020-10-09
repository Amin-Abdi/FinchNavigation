import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class writing {	

	public static void Write()  {
		//try catch for catching any errors
		try {
			LocalDateTime now = LocalDateTime.now();

			//formatting the time into the format HH:MM:SS
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String newTime = now.format(formatter);

			FileWriter writehandle;

			//declaring the name of the file
			writehandle = new FileWriter("data");

			BufferedWriter bw = new BufferedWriter(writehandle);
			bw.write("The current time is " + newTime);
			bw.newLine();

			//checks if any commands has been executed if not then it will tell the user "No commands executed"
			if(FinchNavigate.WriteCommands.isEmpty()) {
				System.out.println("No commands received yet");
			}else {
				bw.write("Commands received:" + FinchNavigate.WriteCommands);
				bw.newLine();
				System.out.println(
						"The current time and all the executed commands have been written into the specified file \n");
			}

			bw.close(); //closes the BufferedWriter

			writehandle.close();
		} catch (IOException e) {
			//  catch block
			e.printStackTrace();
		} //closes the writehandle




	}
}

