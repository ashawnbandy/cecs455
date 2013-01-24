package games.poker;

import java.util.ArrayList;
import java.util.TreeMap;

public class PreviousGames {
	public class Game {
		public TreeMap<Integer,Integer> dropCard=new TreeMap<Integer,Integer>();
		public TreeMap<Integer,Integer> card=new TreeMap<Integer,Integer>();
		public ArrayList<Integer> positions=new ArrayList<Integer>();
		public ArrayList<Integer> plays=new ArrayList<Integer>();
	}
	public void addGame(TreeMap<Integer,Integer> d, TreeMap<Integer,Integer> c, ArrayList<Integer> pos, ArrayList<Integer> p) {
		Game game=new Game(); game.dropCard.putAll(d); game.card.putAll(c);
		game.positions.addAll(pos); game.plays.addAll(p);
		previous.add(game);
	}
	public ArrayList<Game> previous=new ArrayList<Game>();
}
