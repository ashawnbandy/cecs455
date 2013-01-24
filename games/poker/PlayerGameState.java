package games.poker;

import java.util.ArrayList;
import java.util.TreeMap;

public class PlayerGameState {
	TreeMap<Integer,Integer> cashRemaining=new TreeMap<Integer,Integer>();
	TreeMap<Integer,Integer> dropped=new TreeMap<Integer,Integer>();
	ArrayList<Integer> cards=new ArrayList<Integer>();
	ArrayList<Integer> positions=new ArrayList<Integer>();
	ArrayList<Integer> playsThusFar=new ArrayList<Integer>();
	PreviousGames previous=new PreviousGames();
}
