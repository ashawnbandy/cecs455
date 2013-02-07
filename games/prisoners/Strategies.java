package games.prisoners;

import java.util.Random;

public class Strategies {
	public class ResultClass {
		boolean valid=false; boolean result; }
	
	private class Player1111 {
		public Boolean play(PreviousGames games) {
			// Cooperate if this is the first move.
			// Otherwise, count the number of times that cooperation has occurred during previous play.
			// If cooperation has occurred at least as much as defection, then cooperate.
			if (games.myPreviousMoves.size()==0) return true;
			int totalTrue=0;
			for (int i=0; i<games.myPreviousMoves.size(); i++)
				totalTrue+=(games.myPreviousMoves.get(i)?1:0);
			for (int i=0; i<games.hisPreviousMoves.size(); i++)
				totalTrue+=(games.hisPreviousMoves.get(i)?1:0);
			return (totalTrue>=games.myPreviousMoves.size());
		}
	}
	
	private class Player2222 {
		public Boolean play(PreviousGames games) {
			return (new Random().nextBoolean());
		}
	}
	
	private class Player3333 {
		public Boolean play(PreviousGames games) {
			return false;
		}
	}
	
	// TODO: You should write a class for PlayerXXXX (where XXXX is the last 4 digits of your student ID) and
	// execute this code. You will only turn in a file that consists of a player class that you write yourself.

	public class PlayerThread extends Thread {
		public ResultClass result; private PreviousGames games; private Integer id;
		public PlayerThread(PreviousGames g, ResultClass r, int identification) { games=g; result=r; id=identification; }
		public void run() {
			try {
			switch (id) {
			case 1111: result.result=new Player1111().play(games); break;
			case 2222: result.result=new Player2222().play(games); break;
			case 3333: result.result=new Player3333().play(games); break;
			default: throw new RuntimeException("Unsupported player."); }
			result.valid=true; }
			catch (Exception e) { result.valid=false; }
		}
	}
	@SuppressWarnings("deprecation")
	public Boolean getResult(int id, PreviousGames games) {
		ResultClass result=new ResultClass();
		PlayerThread thread=new PlayerThread(games,result,id);
		thread.start();
		for (int iterations=0; iterations<30; iterations++) { // 3 second maximum
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
			if (result.valid) return result.result; }
		thread.stop();
		return null;
	}
}
