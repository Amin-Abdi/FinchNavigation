import edu.cmu.ri.createlab.terk.robot.finch.Finch;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.ArrayList;

public class FinchNavigate {

	//declaring the variable as global so that they can be used by any method
	static Finch myFinch = new Finch();
	static int speed;
	static int duration;
	static Scanner scanner = new Scanner(System.in);
	//this is an arraylist of arralists which will store the each commands arraylist inside it
	static ArrayList<ArrayList<String>> log = new ArrayList<ArrayList<String>>();

	static ArrayList<Integer> distance = new ArrayList<>();
	static String stringDuration;
	static String stringSpeed;
	static int num;
	static ArrayList<String> WriteCommands = new ArrayList<String>();
	// To prevent a resource leak i have declared the bufferedreader outside of the main method as Static.
	private static BufferedReader br;

	public static void main(String[] args) throws IOException {

		//this starts the timer for calculating how long the program is running for
		long StartTime = System.currentTimeMillis();

		boolean exit = false;

		while (!exit) {
			System.out.println(
					"\n				Enter your command:  \n||   F = Forwards	B = Backwards	 R = Right turn    L = Left turn		|| \n||   W = write current time and commands to a file   T = retrace the commands executed  ||\n||   X = read commands from a file and execute them 					|| \n||   Q = terminate  D = display the total distance travelled in cm 			||\n");
			String choice = scanner.next().toUpperCase();  //this converts every user input to uppercase......

			switch (choice) {
			case "F":
				System.out.println("You have entered F command\n");
				execution();
				Movements.Forward(duration, speed);

				break;

			case "B":
				System.out.println("You have entered B command\n");
				execution();
				Movements.Backward(duration, speed);

				break;

			case "R":
				System.out.println("You have entered R command\n");
				myFinch.setLED(Color.green);
				myFinch.setWheelVelocities(90, -90, 1000);

				execution();
				Movements.right(duration, speed);

				break;

			case "L":
				System.out.println("You have entered L command\n");
				myFinch.setLED(Color.yellow);
				myFinch.setWheelVelocities(-90, 90, 1000);

				execution();
				Movements.left(duration, speed);

				break;

			case "Q":
				//stops the timer
				long EndTime = System.currentTimeMillis();
				//formats the timer to a whole number
				NumberFormat formatter = new DecimalFormat("#");
				//dividing the result by 1000 to get seconds
				System.out.println("This program has ran for " + formatter.format((EndTime - StartTime) / 1000) + " seconds");				
				System.out.println("Exiting program.....");
				exit = true;
				break;

			case "T":
				System.out.println("You have entered T command\n");
				System.out.println("Enter number");
				num = Integer.valueOf(scanner.nextInt());

				retrace(num);



				WriteCommands.add("T");				
				WriteCommands.add(String.valueOf(num));

				break;

			case "W":
				System.out.println("You have entered W command\n");
				//calls the write method from the writing class
				writing.Write();


				WriteCommands.add("W");
				break;

			case "X":
				System.out.println("You have entered X command\n");
				reading();

				WriteCommands.add("X");
				break;

			case "D":
				System.out.println("You have entered D command\n");
				distance();
				WriteCommands.add("D");
				break;
			
			default:
				System.out.println("Error: command not recognised. Enter a valid command");
				break;

			}
		}
	}

	public static void retrace(int num) {
		// Outer Array A is a counter that counts the number of retraced steps once it gets to the num entered then it will stop
		int count = 0;

		//checks if the log arrylist is empty 
		if(log.isEmpty()) {
			System.out.println("There are no commands to execute. Please do some commands to be able to retrace movements");
		}else {
			//checks if the num (number of commands to retrace) is larger than the commands executed
			if (num > log.size()) {
				System.out.println("The number entered is larger than the number of commands previously executed");
			}
			else {
				//for loop for going through the arraylist backwards
				for (int i = log.size()-1; i >	(log.size()-1)- num; i--) {

					//while the number of retraced steps is not equal to the number entered then continue, else stop
					if (count != num) {						
						//calls the readingcommands method with the parameters being the duration, then speed then the command
						readingcommands(log.get(i).get(1), log.get(i).get(2), log.get(i).get(0));
					}
					//adding one to count(the number of times being retraced)
					count++;
				}
			}
		}
	}

	//this method is for converting the duration and speed into String so that they can be stored in the string arraylist
	public static void convert() {
		stringDuration = new Integer(duration).toString();
		stringSpeed = new Integer(speed).toString();
	}

	//this is for checking if the duration is more than 6 seconds
	public static void DurationValidation() {
		while (duration > 6) {
			System.out.println("Duration out of bounds. Should be no more than 6 sec");
			System.out.println("Enter duration ");
			duration = scanner.nextInt();
		}
	}
	
	//this is for checking if the speed is more than 200
	public static void SpeedValidation() {
		while (speed > 200) {
			System.out.println("speed out of bounds. Should be less than 200");
			System.out.println("Enter speed ");
			speed = scanner.nextInt();
		}
	}
	
	//This method is for scanning the duration and speed that the user inputs
	private static void execution() {
		System.out.println("Enter duration ");
		duration = scanner.nextInt();
		DurationValidation();
		System.out.println("Enter speed ");
		speed = scanner.nextInt();
		SpeedValidation();
	}

	public static void reading() throws IOException {

		//catching any error that might be present (for example the file name could be wrong) instead of crashing the whole program
		try {
			//checks if file is empty
			File CheckEmpty = new File("READ");
			if (CheckEmpty.length() == 0) {
				System.out.println("File is empty ...");
			}else {
				FileReader readhandle = new FileReader("READ");
				//getting the path of the file
				Path path = Paths.get("READ");
				br = new BufferedReader(readhandle);
				String s;
				int linecount = 0;

				//counting the number of lines in the text file
				linecount = (int) Files.lines(path).count();
				
				//only execuutes if the number of lines are more than or equal to 3(number of commands are more than or equal to 3)
				if (linecount >= 3) {
					while ((s = br.readLine()) != null) // Reading Content from the file
					{						
						read(s);
					}
				} else {
					System.out.println("Commands must be more than or equal to 3 to be executed");
				}
				readhandle.close();
			}}catch (java.io.FileNotFoundException e) {
				e.printStackTrace();
				//if the file is not found then the following message is printed
				System.err.print("The file could not be found. Please check if the specified path or name of the file is correct \n");
			}

	}

	//this is for calculating the total distance travelled 
	public static void distance() {

		int sum = 0;
		for (int i = 0; i < distance.size(); i++) {
			sum += distance.get(i); // sum = sum + distance.get(i)
		}
		System.out.println("The estimated total distance travelled is " + sum + "cm \n");
	}

	//passing the method with the parameters being a String because in the arraylist is type String
	public static void readingcommands(String duration, String speed, String command) {


		int duration1 = Integer.parseInt(duration);
		int speed1 = Integer.parseInt(speed);

		//switch case for the commands encountered and then calling its respective method
		switch (command) {
		case "F":

			ReForward(duration1,speed1);

			break;

		case "B":

			ReBackward(duration1,speed1);

			break;

		case "R":

			ReRight(duration1,speed1);

			break;

		case "L":

			ReLeft(duration1,speed1);

			break;

		}

	}

	public static void read(String s) {

		//initialising the variables: num(number of commands to be retraced), speed and duration
		int num = 0;
		int duration = 0, speed = 0;
		String command = s.substring(0, 1).toUpperCase(); // getting first index of the string
		String[] list = s.split(" "); // splitting the list by white spaces

		if (command.contains("T")) { // if the first index contains "T" then set the second index as num
			num = Integer.valueOf(list[1]);
		} else {
			duration = Integer.valueOf(list[1]); //else set the second index as duration and the third as speed
			speed = Integer.valueOf(list[2]);
		}

		switch (command) {
		case "F":
			ReForward(duration,speed);

			//creating a new arraylist for each case
			ArrayList<String> commandF = new ArrayList<String>();
			commandF.add("F");
			commandF.add(String.valueOf(duration));
			commandF.add(String.valueOf(speed));

			log.add(commandF); //adding the created arrylist into a bigger arraylist called log

			break;

		case "B":

			ReBackward(duration, speed);

			ArrayList<String> commandB = new ArrayList<String>();
			commandB.add("B");
			commandB.add(String.valueOf(duration));
			commandB.add(String.valueOf(speed));

			log.add(commandB);


			break;

		case "R":
			ReRight(duration, speed);

			ArrayList<String> commandR = new ArrayList<String>();
			commandR.add("R");
			commandR.add(String.valueOf(duration));
			commandR.add(String.valueOf(speed));

			log.add(commandR);

			break;

		case "L":
			ReLeft(duration, speed);

			ArrayList<String> commandL = new ArrayList<String>();
			commandL.add("L");
			commandL.add(String.valueOf(duration));
			commandL.add(String.valueOf(speed));

			log.add(commandL);

			break;
		case "T":
			//calling retrace method with num(number of commands) as its parameters
			retrace(num);

			break;

		}

	}
	//this method is for adding the StringDuration and StringSpeed into the Writecommands arraylist so that it can be later written to a file
	public static void WritingArray() {
		WriteCommands.add(stringDuration);
		WriteCommands.add(stringSpeed);
	}
	public static void ReForward(int duration, int speed) {

		myFinch.setLED(Color.red);
		myFinch.setWheelVelocities(speed, speed, duration * 1000);

		distance.add((speed * duration) / 10);
	}
	public static void ReBackward(int duration, int speed) {

		myFinch.setLED(Color.blue);
		myFinch.setWheelVelocities(-speed, -speed, duration * 1000);
		distance.add((speed * duration) / 10);
	}
	public static void ReRight(int duration, int speed) {

		myFinch.setLED(Color.green);
		myFinch.setWheelVelocities(90, -90, 1000);
		myFinch.setWheelVelocities(speed, speed, duration * 1000);
		distance.add((speed * duration) / 10);

	}
	public static void ReLeft(int duration, int speed) {

		myFinch.setLED(Color.yellow);
		myFinch.setWheelVelocities(-90, 90, 1000);
		myFinch.setWheelVelocities(speed, speed, duration * 1000);
		distance.add((speed * duration) / 10);

	}

}
