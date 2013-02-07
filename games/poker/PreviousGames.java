package games.poker;

import java.util.ArrayList;

public class PreviousGames {
	public class Game {
		public Integer myDropCard=-1;
		public Integer hisDropCard=-1;
		public Integer myPlayCard=-1;
		public Integer hisPlayCard=-1;
		public Boolean IPlayFirst=true;
		public ArrayList<Integer> plays=new ArrayList<Integer>();
	}
	public void addGame(int mdc, int hdc, int mpc, int hpc, boolean first, ArrayList<Integer> p) {
		Game game=new Game();
		game.myDropCard=mdc; game.hisDropCard=hdc;
		game.myPlayCard=mpc; game.hisPlayCard=hpc;
		game.IPlayFirst=first;
		game.plays.addAll(p);
		previous.add(game);
	}
	public ArrayList<Game> previous=new ArrayList<Game>();
}
