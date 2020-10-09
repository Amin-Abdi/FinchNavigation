import java.awt.Color;
import java.util.ArrayList;

public class Movements {

	public static void Forward(int duration, int speed) {

		FinchNavigate.myFinch.setLED(Color.red);
		FinchNavigate.myFinch.setWheelVelocities(speed, speed, duration * 1000);

		FinchNavigate.distance.add((speed * duration) / 10);

		FinchNavigate.convert();

		ArrayList<String> commands = new ArrayList<String>();
		commands.add("F");
		commands.add(FinchNavigate.stringDuration);
		commands.add(FinchNavigate.stringSpeed);
		FinchNavigate.log.add(commands);



		FinchNavigate.WriteCommands.add("F");
		FinchNavigate.WritingArray();

	}
	public static void Backward(int duration, int speed) {

		FinchNavigate.myFinch.setLED(Color.blue);
		FinchNavigate.myFinch.setWheelVelocities(-speed, -speed, duration * 1000);
		FinchNavigate.distance.add((speed * duration) / 10);

		FinchNavigate.convert();

		//creating a new arraylist for each movement so that they can each be recorded separately
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("B");
		commands.add(FinchNavigate.stringDuration);
		commands.add(FinchNavigate.stringSpeed);
		FinchNavigate.log.add(commands);

		FinchNavigate.WriteCommands.add("B");
		FinchNavigate.WritingArray();
	}
	public static void right(int duration, int speed) {

		FinchNavigate.myFinch.setWheelVelocities(speed, speed, duration * 1000);
		FinchNavigate.distance.add((speed * duration) / 10);

		FinchNavigate.convert();

		ArrayList<String> commands = new ArrayList<String>();
		commands.add("R");
		commands.add(FinchNavigate.stringDuration);
		commands.add(FinchNavigate.stringSpeed);
		FinchNavigate.log.add(commands);

		FinchNavigate.WriteCommands.add("R");
		FinchNavigate.WritingArray();
	}
	public static void left(int duration, int speed) {

		FinchNavigate.myFinch.setWheelVelocities(speed, speed, duration * 1000);
		FinchNavigate.distance.add((speed * duration) / 10);

		FinchNavigate.convert();

		ArrayList<String> commands = new ArrayList<String>();
		commands.add("L");
		commands.add(FinchNavigate.stringDuration);
		commands.add(FinchNavigate.stringSpeed);
		FinchNavigate.log.add(commands);

		FinchNavigate.WriteCommands.add("L");
		FinchNavigate.WritingArray();
	}
}
