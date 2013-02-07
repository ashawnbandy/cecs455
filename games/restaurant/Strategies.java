package games.restaurant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class Strategies {
	public class ResultClass {
		boolean valid=false; Integer result; }
	
	private class Player1111 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			// Pick the restaurant that has the best experience assuming that nobody else picks it.
			int best=0; int max=-1;
			for (int i=0; i<choices.size(); i++)
				if (choices.get(i).get(choices.get(i).size()-1)>max) { best=i; max=choices.get(i).get(choices.get(i).size()-1); }
			return best;
		}
	}

	private class Player2222 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			// Pick the first restaurant you come to.
			return 0;
		}
	}
	
	private class Player3333 {
		public Integer play(PreviousGames games, ArrayList<ArrayList<Integer>> choices) {
			// Pick the restaurant that has been chosen most by everyone else previously.
			TreeMap<Integer,Integer> numPicks=new TreeMap<Integer,Integer>();
			for (int i=0; i<games.previous.size(); i++) {
				for (int j=0; j<games.previous.get(i).otherChoices.size(); j++) {
					if (!numPicks.containsKey(games.previous.get(i).otherChoices.get(j)))
						numPicks.put(games.previous.get(i).otherChoices.get(j), 1);
					else numPicks.put(games.previous.get(i).otherChoices.get(j), numPicks.get(games.previous.get(i).otherChoices.get(j))+1);
				}
			}
			int maxNum=-1; int max=-1;
			Iterator<Integer> numIter=numPicks.keySet().iterator();
			while (numIter.hasNext()) {
				Integer current=numIter.next();
				if (numPicks.get(current)>maxNum) { maxNum=numPicks.get(current); max=current; } }
			return ((max==-1)?0:max);
		}
	}
	// TODO: You should write a class for PlayerXXXX (where XXXX is the last 4 digits of your student ID) and
	// execute this code. You will only turn in a file that consists of a player class that you write yourself.

	public class PlayerThread extends Thread {
		public ResultClass result; private PreviousGames games; ArrayList<ArrayList<Integer>> choices; private Integer id;
		public PlayerThread(PreviousGames g, ResultClass r, ArrayList<ArrayList<Integer>> c, int identification) { games=g; result=r; choices=c; id=identification; }
		public void run() {
			try {
			switch (id) {
			case 1111: result.result=new Player1111().play(games,choices); break;
			case 2222: result.result=new Player2222().play(games,choices); break;
			case 3333: result.result=new Player3333().play(games,choices); break;
			default: throw new RuntimeException("Unsupported player."); }
			result.valid=true; }
			catch (Exception e) { result.valid=false; }
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
