package games.restaurant;

import java.util.ArrayList;
import java.util.TreeMap;

public class PreviousGames {
	public class Game {
		public ArrayList<ArrayList<Integer>> ratings=new ArrayList<ArrayList<Integer>>();
		public TreeMap<Integer,Integer> choices=new TreeMap<Integer,Integer>();
	}
	public void addGame(ArrayList<ArrayList<Integer>> r, TreeMap<Integer,Integer> c) {
		Game game=new Game(); game.ratings=r; game.choices=c;
		previous.add(game);
	}
	ArrayList<Game> previous=new ArrayList<Game>();
}
