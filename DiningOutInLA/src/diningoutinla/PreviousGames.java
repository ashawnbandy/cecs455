/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diningoutinla;

import java.util.ArrayList;

public class PreviousGames {
	public class Game {
		public ArrayList<ArrayList<Integer>> ratings=new ArrayList<ArrayList<Integer>>();
		public ArrayList<Integer> otherChoices=new ArrayList<Integer>();
		public Integer myChoice;
	}
	public void addGame(ArrayList<ArrayList<Integer>> r, ArrayList<Integer> others, Integer my) {
		Game game=new Game(); game.ratings=r;
		game.otherChoices=others; game.myChoice=my; 
		previous.add(game);
	}
	ArrayList<Game> previous=new ArrayList<Game>();
}

