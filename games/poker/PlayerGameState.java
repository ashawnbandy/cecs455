package games.poker;

import java.util.ArrayList;

public class PlayerGameState {
	public Integer myCashRemaining=0;
	public Integer hisCashRemaining=0;
	public Integer myDropCard=-1;
	public Integer hisDropCard=-1;
	public Integer myPlayCard=-1;
	public Boolean IPlayFirst=true;
	public ArrayList<Integer> playsThusFar=new ArrayList<Integer>();
	public ArrayList<Integer> cards=new ArrayList<Integer>();
	PreviousGames previous=new PreviousGames();
}
