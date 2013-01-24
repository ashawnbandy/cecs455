package games;


public class MainClass {
	public static void main(String[] args) {
		// TODO: Each of these directories should be changed to reflect the place where you want your scores and logs to go.
		games.prisoners.PlayGame prisoners=new games.prisoners.PlayGame();
		prisoners.runGame("C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\prisonerScore.txt",
				"C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\prisonerLog.txt");
		games.restaurant.PlayGame restaurant=new games.restaurant.PlayGame();
		restaurant.runGame("C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\restaurantScore.txt",
				"C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\restaurantLog.txt");
		games.poker.PlayGame poker=new games.poker.PlayGame();
		poker.runGame("C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\pokerScore.txt",
				"C:\\Documents and Settings\\Darin\\My Documents\\eclipse-java-juno-SR1-win32\\eclipse\\workspace\\GameAssignments\\pokerLog.txt");
	}
}
