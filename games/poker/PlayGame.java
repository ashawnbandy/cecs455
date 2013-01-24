package games.poker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

public class PlayGame {
	private TreeMap<Integer,Integer> scores=new TreeMap<Integer,Integer>();
	public int[] id={1111,2222,3333};

	private int computeStakes(ArrayList<Integer> plays, int position) {
		// This function will be written to compute the stakes of a given player assuming the sequence of plays and given the player's position (either 0 or 1)
		int total=1;
		for (int i=0; i<plays.size(); i++) {
			if ((i%2)==position) { // your turn
				if ((i>0)&&(plays.get(i-1)==2)&&(plays.get(i)!=-1)) total++;
				if (plays.get(i)==2) total++;
				if (plays.get(i)==-1) break; } }
		return total;
	}
	public void runGame(String scoreFilename, String logFilename) {
		BufferedWriter logw;
		try {
			BufferedReader br=new BufferedReader(new FileReader(scoreFilename));
			String currentLine;
			while ((currentLine=br.readLine())!=null) {
				int id=1000*(currentLine.charAt(0)-'0')+100*(currentLine.charAt(1)-'0')+10*(currentLine.charAt(2)-'0')+(currentLine.charAt(3)-'0');
				int score=0; int position=5;
				while (position<currentLine.length()) {
					score=(score*10)+(currentLine.charAt(position)-'0'); position++; }
				scores.put(id, score); }
			br.close();
			
			logw=new BufferedWriter(new FileWriter(logFilename));
		
			Random random=new Random();
			Strategies strategies=new Strategies();
			for (int player1=0; player1<id.length; player1++)
				for (int player2=player1+1; player2<id.length; player2++) {
					PreviousGames games=new PreviousGames();
					
					// initialize a player game state for each player
					TreeMap<Integer,PlayerGameState> playerState=new TreeMap<Integer,PlayerGameState>();
					playerState.put(id[player1], new PlayerGameState());
					playerState.put(id[player2], new PlayerGameState());
					
					// initialize everyone's money
					TreeMap<Integer,Integer> cashRemaining=new TreeMap<Integer,Integer>();
					cashRemaining.put(id[player1], 1000); cashRemaining.put(id[player2], 1000);
					playerState.get(id[player1]).cashRemaining=cashRemaining;
					playerState.get(id[player2]).cashRemaining=cashRemaining;
											
					// play the games
					for (int iterations=0; iterations<500; iterations++) {
						// make sure that both players have enough money to play
						if ((playerState.get(id[player1]).cashRemaining.get(id[player1])==0)||
								(playerState.get(id[player2]).cashRemaining.get(id[player2])==0)) break;
						
						// determine who plays first
						ArrayList<Integer> positions=new ArrayList<Integer>();
						if (random.nextBoolean()) { positions.add(id[player1]); positions.add(id[player2]); }
						else { positions.add(id[player2]); positions.add(id[player1]); }
						playerState.get(id[player1]).positions=positions;
						playerState.get(id[player2]).positions=positions;
						System.out.println("Positions: "+positions.toString());
						logw.write("Positions: "+positions.toString()); logw.newLine();
						
						// deal the cards
						playerState.get(id[player1]).cards=new ArrayList<Integer>();
						playerState.get(id[player1]).cards.add(random.nextInt(20)+1);
						playerState.get(id[player1]).cards.add(random.nextInt(20)+1);
						playerState.get(id[player2]).cards=new ArrayList<Integer>();
						playerState.get(id[player2]).cards.add(random.nextInt(20)+1);
						playerState.get(id[player2]).cards.add(random.nextInt(20)+1);
						System.out.println("Cards for "+id[player1]+": "+playerState.get(id[player1]).cards.toString());
						System.out.println("Cards for "+id[player2]+": "+playerState.get(id[player2]).cards.toString());
						logw.write(""+id[player1]+": "+playerState.get(id[player1]).cards.toString()+" ");
						logw.write(""+id[player2]+": "+playerState.get(id[player2]).cards.toString()); logw.newLine();
						
						// get the initial plays, if applicable
						TreeMap<Integer,Integer> dropCard=new TreeMap<Integer,Integer>();
						Boolean initialPlay=strategies.getInitialResult(id[player1], playerState.get(id[player1]));
						if (initialPlay==null) {
							playerState.get(id[player1]).cashRemaining.put(id[player1], 0);
							playerState.get(id[player2]).cashRemaining.put(id[player1], 0);	}
						int toDrop=((playerState.get(id[player1]).cashRemaining.get(id[player1])>0)?((initialPlay)?1:0):random.nextInt(2));
						dropCard.put(id[player1], playerState.get(id[player1]).cards.get(toDrop));
						playerState.get(id[player1]).cards.remove(toDrop);
						// player2
						initialPlay=strategies.getInitialResult(id[player2], playerState.get(id[player2]));
						if (initialPlay==null) {
							playerState.get(id[player1]).cashRemaining.put(id[player2], 0);
							playerState.get(id[player2]).cashRemaining.put(id[player2], 0);	}
						toDrop=((playerState.get(id[player2]).cashRemaining.get(id[player2])>0)?((initialPlay)?1:0):random.nextInt(2));
						dropCard.put(id[player2], playerState.get(id[player2]).cards.get(toDrop));
						playerState.get(id[player2]).cards.remove(toDrop);
						System.out.println("Dropped: "+dropCard.toString());
						logw.write("Dropped: "+dropCard.toString()); logw.newLine();
						playerState.get(id[player1]).dropped=dropCard;
						playerState.get(id[player2]).dropped=dropCard;
						
						// play the game
						boolean done=false; int current=0;
						ArrayList<Integer> plays=new ArrayList<Integer>();
						ArrayList<Integer> possiblePlays=new ArrayList<Integer>();
						Integer play = -2;
						logw.write("Plays: ");
						while (!done) {
							if (playerState.get(positions.get(current)).cashRemaining.get(positions.get(current))<0)
								throw new RuntimeException("This should never happen.");
							if (playerState.get(positions.get(1-current)).cashRemaining.get(positions.get(1-current))<0)
								throw new RuntimeException("This should never happen.");
							
							if ((plays.size()>=2)&&(plays.get(plays.size()-2)==-1)) play=-1;
							else if (playerState.get(positions.get(current)).cashRemaining.get(positions.get(current))==0) play=-1;
							else if (playerState.get(positions.get(current)).cashRemaining.get(positions.get(1-current))==1) play=1;
							else play=strategies.getResult(positions.get(current), playerState.get(positions.get(current)));
							if (play==null) play=-1;
							switch (play) {
							case -1: /* fold */	break;
							case 0: /* pass */
								// if the previous move was a pass, this turns into a call
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==0)
										play=1;
								// check to make sure that you have the money for a pass
								possiblePlays.clear();
								possiblePlays.addAll(playerState.get(positions.get(current)).playsThusFar);
								possiblePlays.add(0);
								if (computeStakes(possiblePlays,current)>playerState.get(positions.get(current)).cashRemaining.get(positions.get(current)))
									play=1;
								if (computeStakes(possiblePlays,1-current)>playerState.get(positions.get(current)).cashRemaining.get(positions.get(1-current)))
									play=1;
								break;
							case 1:	/* call */
								// you are only allowed to call if you are risking 100 units or more OR the previous move was a pass
								boolean valid=false;
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==0)
										valid=true;
								possiblePlays.clear();
								possiblePlays.addAll(playerState.get(positions.get(current)).playsThusFar);
								possiblePlays.add(1);
								if (computeStakes(possiblePlays,current)>=100) valid=true;
								if (!valid) play=0;
								break;
							case 2: /* raise */
								// check to make sure that you have the money for a raise
								possiblePlays.clear();
								possiblePlays.addAll(playerState.get(positions.get(current)).playsThusFar);
								possiblePlays.add(2);
								if (computeStakes(possiblePlays,current)>playerState.get(positions.get(current)).cashRemaining.get(positions.get(current)))
									play=1;
								possiblePlays.add(1);
								if (computeStakes(possiblePlays,1-current)>playerState.get(positions.get(current)).cashRemaining.get(positions.get(1-current)))
									play=1;
								break;
							default: throw new RuntimeException("Unsupported option.");	}
							plays.add(play);
							
							// print out the play
							System.out.print(""+positions.get(current)+": ");
							switch (play) {
							case -1: System.out.println("fold"); logw.write("fold "); break;
							case 0: System.out.println("pass"); logw.write("pass "); break;
							case 1: System.out.println("call"); logw.write("call "); break;
							case 2: System.out.println("raise ("+computeStakes(playerState.get(positions.get(current)).playsThusFar,current)+")"); logw.write("raise "); break;
							default: throw new RuntimeException("Unsupported play."); }
							
							// add this play to the plays
							for (int i=0; i<positions.size(); i++)
								playerState.get(positions.get(i)).playsThusFar.add(play);
							// increment current
							current=1-current;
							// check if we are done: We are done if this is a fold or a call
							if ((play==-1)||(play==1)) done=true;
						}
						logw.newLine();
						
						// As of this point, the game is done.
						// Add this game to the previous games.
						TreeMap<Integer,Integer> card=new TreeMap<Integer,Integer>();
						for (int i=0; i<positions.size(); i++)
							card.put(positions.get(i), playerState.get(positions.get(i)).cards.get(0));
						games.addGame(dropCard, card, positions, playerState.get(positions.get(0)).playsThusFar);
						
						TreeMap<Integer,Integer> stakes=new TreeMap<Integer,Integer>();
						stakes.put(positions.get(0), computeStakes(playerState.get(positions.get(0)).playsThusFar,0));
						stakes.put(positions.get(1), computeStakes(playerState.get(positions.get(1)).playsThusFar,1));
						int totalPot=stakes.get(positions.get(0))+stakes.get(positions.get(1));
						
						// compute the winner(s)
						TreeMap<Integer,Integer> winners=new TreeMap<Integer,Integer>(); int maxCard=0;
						if (play==1) {
							for (int i=0; i<positions.size(); i++)
								if (playerState.get(positions.get(i)).cards.get(0)>maxCard)
								{ maxCard=playerState.get(positions.get(i)).cards.get(0); winners.clear(); winners.put(positions.get(i),0); }
								else if (playerState.get(positions.get(i)).cards.get(0)==maxCard)
									winners.put(positions.get(i),0); 
						} else
							winners.put(positions.get(playerState.get(positions.get(0)).playsThusFar.size()%2), 0);
						for (int i=0; i<totalPot; i++) {
							int counter=random.nextInt(winners.size());
							Iterator<Integer> winIter=winners.keySet().iterator();
							while (counter>0) { winIter.next(); counter--; }
							int x=winIter.next();
							winners.put(x, winners.get(x)+1); }
						System.out.println("Cards- "+id[player1]+":"+playerState.get(id[player1]).cards.get(0)+" "+id[player2]+":"+playerState.get(id[player2]).cards.get(0));
	
						// update the player states
						TreeMap<Integer,Integer> cash=new TreeMap<Integer,Integer>();
						for (int i=0; i<positions.size(); i++)
							if (winners.containsKey(positions.get(i)))
								cash.put(positions.get(i), playerState.get(positions.get(0)).cashRemaining.get(positions.get(i))+winners.get(positions.get(i))-stakes.get(positions.get(i)));
							else cash.put(positions.get(i), playerState.get(positions.get(0)).cashRemaining.get(positions.get(i))-stakes.get(positions.get(i)));
						playerState.get(id[player1]).previous=games;
						playerState.get(id[player1]).cards.clear();
						playerState.get(id[player1]).playsThusFar.clear();
						playerState.get(id[player1]).positions.clear();
						playerState.get(id[player1]).cashRemaining=cash;
						playerState.get(id[player1]).dropped.clear();
						playerState.get(id[player2]).previous=games;
						playerState.get(id[player2]).cards.clear();
						playerState.get(id[player2]).playsThusFar.clear();
						playerState.get(id[player2]).positions.clear();
						playerState.get(id[player2]).cashRemaining=cash;
						playerState.get(id[player2]).dropped.clear();
						
						System.out.println("Cash remaining: "+playerState.get(id[player1]).cashRemaining.toString());
						logw.write("Cash remaining: "+playerState.get(id[player1]).cashRemaining.toString()); logw.newLine();
						logw.write("---"); logw.newLine();
					}
					// update the scores
					scores.put(id[player1], scores.get(id[player1])+playerState.get(id[player1]).cashRemaining.get(id[player1]));
					scores.put(id[player2], scores.get(id[player2])+playerState.get(id[player2]).cashRemaining.get(id[player2]));				
			}
			
			logw.close();
		
			BufferedWriter bw=new BufferedWriter(new FileWriter(scoreFilename));
			Iterator<Integer> idIter=scores.keySet().iterator();
			while (idIter.hasNext()) {
				int id=idIter.next(); int score=scores.get(id);
				currentLine=new Integer(id/1000).toString();
				currentLine+=new Integer((id/100)%10).toString();
				currentLine+=new Integer((id/10)%10).toString();
				currentLine+=new Integer(id%10).toString();
				currentLine+=" ";
				ArrayList<Integer> scoreDigits=new ArrayList<Integer>();
				while (score!=0) {
					scoreDigits.add(score%10); score/=10; }
				if (scoreDigits.isEmpty()) scoreDigits.add(0);
				for (int i=0; i<scoreDigits.size(); i++)
					currentLine+=new Integer(scoreDigits.get(scoreDigits.size()-i-1)).toString();
				currentLine+="\r\n";
				bw.write(currentLine);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
