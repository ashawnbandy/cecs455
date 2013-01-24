package games.restaurant;

import java.util.ArrayList;
import java.util.Random;

public class Strategies {
	public class ResultClass {
		boolean valid=false; Integer result; }
	
	private class Player1111 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			int best=0; int max=-1;
			for (int i=0; i<choices.size(); i++)
				if (choices.get(i).get(choices.get(i).size()-1)>max) { best=i; max=choices.get(i).get(choices.get(i).size()-1); }
			return best;
		}
	}
	private class Player2222 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			Random r=new Random();
			return r.nextInt(choices.size());
		}
	}
	private class Player3333 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			int expectedPatrons=choices.get(0).size()/choices.size();
			int best=0; int max=-1;
			for (int i=0; i<choices.size(); i++)
				if (choices.get(i).get(expectedPatrons)>max) { best=i; max=choices.get(i).get(expectedPatrons); }
			return best;
		}
	}

	public class PlayerThread extends Thread {
		public ResultClass result; private PreviousGames games; ArrayList<ArrayList<Integer>> choices; private Integer id;
		public PlayerThread(PreviousGames g, ResultClass r, ArrayList<ArrayList<Integer>> c, int identification) { games=g; result=r; choices=c; id=identification; }
		public void run() {
			switch (id) {
			case 1111: result.result=new Player1111().play(games,choices); result.valid=true; break;
			case 2222: result.result=new Player2222().play(games,choices); result.valid=true; break;
			case 3333: result.result=new Player3333().play(games,choices); result.valid=true; break;
			default: throw new RuntimeException("Unsupported player."); }
		}
	}
	@SuppressWarnings("deprecation")
	public Integer getResult(int id, PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
		ResultClass result=new ResultClass();
		PlayerThread thread=new PlayerThread(games,result,choices,id);
		thread.start();
		for (int iterations=0; iterations<30; iterations++) { // 3 second maximum
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
			if (result.valid) return result.result; }
		thread.stop();
		return null;
	}

}
