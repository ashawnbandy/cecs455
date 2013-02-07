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
	// TODO: add the id of any extra players you'd like to
	public int[] id={1111,2222,3333};

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
					// initialize a player game state for each player
					TreeMap<Integer,PlayerGameState> playerState=new TreeMap<Integer,PlayerGameState>();
					playerState.put(id[player1], new PlayerGameState());
					playerState.put(id[player2], new PlayerGameState());
					
					// initialize everyone's money
					TreeMap<Integer,Integer> cashRemaining=new TreeMap<Integer,Integer>();
					cashRemaining.put(id[player1], 1000); cashRemaining.put(id[player2], 1000);
					playerState.get(id[player1]).myCashRemaining=cashRemaining.get(id[player1]);
					playerState.get(id[player1]).hisCashRemaining=cashRemaining.get(id[player2]);
					playerState.get(id[player2]).myCashRemaining=cashRemaining.get(id[player2]);
					playerState.get(id[player2]).hisCashRemaining=cashRemaining.get(id[player1]);
											
					// play the games
					for (int iterations=0; iterations<500; iterations++) {
						// make sure that both players have enough money to play
						if ((playerState.get(id[player1]).myCashRemaining==0)||
								(playerState.get(id[player2]).myCashRemaining==0)) break;
						
						// ante up
						TreeMap<Integer,Integer> potMap=new TreeMap<Integer,Integer>();
						playerState.get(id[player1]).myCashRemaining--; playerState.get(id[player2]).hisCashRemaining--;
						playerState.get(id[player2]).myCashRemaining--; playerState.get(id[player1]).hisCashRemaining--;
						potMap.put(id[player1], 1); potMap.put(id[player2], 1);
						
						// determine who plays first
						ArrayList<Integer> positions=new ArrayList<Integer>();
						if (random.nextBoolean()) { positions.add(id[player1]); positions.add(id[player2]); }
						else { positions.add(id[player2]); positions.add(id[player1]); }
						playerState.get(id[player1]).IPlayFirst=(positions.get(0)==id[player1]);
						playerState.get(id[player2]).IPlayFirst=(positions.get(0)!=id[player1]);
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
						Boolean initialPlay=strategies.getInitialResult(id[player1], playerState.get(id[player1]));
						if (initialPlay==null) {
							playerState.get(id[player1]).myCashRemaining=0;
							playerState.get(id[player2]).hisCashRemaining=0;	}
						int toDrop=((playerState.get(id[player1]).myCashRemaining>0)?((initialPlay)?1:0):random.nextInt(2));
						playerState.get(id[player1]).myDropCard=playerState.get(id[player1]).cards.get(toDrop);
						playerState.get(id[player1]).myPlayCard=playerState.get(id[player1]).cards.get(1-toDrop);
						playerState.get(id[player2]).hisDropCard=playerState.get(id[player1]).cards.get(toDrop);
						// player2
						initialPlay=strategies.getInitialResult(id[player2], playerState.get(id[player2]));
						if (initialPlay==null) {
							playerState.get(id[player1]).hisCashRemaining=0;
							playerState.get(id[player2]).myCashRemaining=0;	}
						toDrop=((playerState.get(id[player2]).myCashRemaining>0)?((initialPlay)?1:0):random.nextInt(2));
						playerState.get(id[player2]).myDropCard=playerState.get(id[player2]).cards.get(toDrop);
						playerState.get(id[player2]).myPlayCard=playerState.get(id[player2]).cards.get(1-toDrop);
						playerState.get(id[player1]).hisDropCard=playerState.get(id[player2]).cards.get(toDrop);
						System.out.println("Dropped: "+id[player1]+"->"+playerState.get(id[player1]).myDropCard+", "+id[player2]+"->"+playerState.get(id[player2]).myDropCard);
						logw.write("Dropped: "+id[player1]+"->"+playerState.get(id[player1]).myDropCard+", "+id[player2]+"->"+playerState.get(id[player2]).myDropCard); logw.newLine();
						
						// play the game
						boolean done=false; int current=0;
						ArrayList<Integer> plays=new ArrayList<Integer>();
						Integer play = -2;
						logw.write("Plays: ");
						while (!done) {
							if ((playerState.get(positions.get(current)).myCashRemaining<0)||(playerState.get(positions.get(current)).hisCashRemaining<0))
								throw new RuntimeException("This should never happen.");
							if ((playerState.get(positions.get(1-current)).myCashRemaining<0)||(playerState.get(positions.get(1-current)).hisCashRemaining<0))
								throw new RuntimeException("This should never happen.");
							
							play=strategies.getResult(positions.get(current), playerState.get(positions.get(current)));
							if (play==null) play=-1;
							switch (play) {
							case -1: /* fold */	break;
							case 0: /* pass */
								// If the previous move was a pass, this turns into a call
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==0)
										play=1;
								// If the previous move was a raise, then you need to throw a unit into the pot (or you fold).
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==2)
										if (playerState.get(positions.get(current)).myCashRemaining==0)
											throw new RuntimeException("This should never happen.");
										else {
											playerState.get(positions.get(current)).myCashRemaining--;
											playerState.get(positions.get(1-current)).hisCashRemaining--;
											potMap.put(positions.get(current), potMap.get(positions.get(current))+1); }
								break;
							case 1:	/* call */
								// If the previous move was a raise, then you need to throw a unit into the pot (or you fold).
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==2)
										if (playerState.get(positions.get(current)).myCashRemaining==0)
											throw new RuntimeException("This should never happen.");
										else {
											playerState.get(positions.get(current)).myCashRemaining--;
											playerState.get(positions.get(1-current)).hisCashRemaining--;
											potMap.put(positions.get(current), potMap.get(positions.get(current))+1); }
								break;
							case 2: /* raise */
								// If the previous move was a raise, then you need to throw a unit into the pot (or you fold).
								if (playerState.get(positions.get(current)).playsThusFar.size()>=1)
									if (playerState.get(positions.get(current)).playsThusFar.get(playerState.get(positions.get(current)).playsThusFar.size()-1)==2)
										if (playerState.get(positions.get(current)).myCashRemaining==0)
											throw new RuntimeException("This should never happen.");
										else {
											playerState.get(positions.get(current)).myCashRemaining--;
											playerState.get(positions.get(1-current)).hisCashRemaining--;
											potMap.put(positions.get(current), potMap.get(positions.get(current))+1); }
								// check to make sure that both players have money for a raise
								if ((playerState.get(positions.get(current)).myCashRemaining==0)||
										(playerState.get(positions.get(current)).hisCashRemaining==0))
									play=1;
								else { // throw a token into the pot
									playerState.get(positions.get(current)).myCashRemaining--;
									playerState.get(positions.get(1-current)).hisCashRemaining--;
									potMap.put(positions.get(current), potMap.get(positions.get(current))+1); }
								break;
							default: throw new RuntimeException("Unsupported option.");	}
							plays.add(play);
							
							// print out the play
							System.out.print(""+positions.get(current)+": ");
							switch (play) {
							case -1: System.out.println("fold- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); logw.write("fold- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); break;
							case 0: System.out.println("pass- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); logw.write("pass- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); break;
							case 1: System.out.println("call- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); logw.write("call- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); break;
							case 2: System.out.println("raise- ("+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); logw.write("raise- "+(potMap.get(positions.get(current))+potMap.get(positions.get(1-current)))); break;
							default: throw new RuntimeException("Unsupported play."); }
							logw.newLine();
							
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
						
						// compute the winner(s)
						if (play==-1) { // who folded?
							int folded;
							int pot=potMap.get(id[player1])+potMap.get(id[player2]);
							if ((playerState.get(positions.get(0)).IPlayFirst&&((playerState.get(positions.get(0)).playsThusFar.size()&1)==1))||
									(!playerState.get(positions.get(0)).IPlayFirst&&(playerState.get(positions.get(0)).playsThusFar.size()&1)==0))
								folded=0; else folded=1;
							playerState.get(positions.get(1-folded)).myCashRemaining+=pot;
							playerState.get(positions.get(folded)).hisCashRemaining+=pot;
						} else {
							int winner=-1;
							if (playerState.get(positions.get(0)).myPlayCard>playerState.get(positions.get(1)).myPlayCard) winner=0;
							else if (playerState.get(positions.get(1)).myPlayCard>playerState.get(positions.get(0)).myPlayCard) winner=1;
							if (winner==-1) { // tie game
								playerState.get(positions.get(0)).myCashRemaining+=potMap.get(positions.get(0));
								playerState.get(positions.get(1)).myCashRemaining+=potMap.get(positions.get(1));
								playerState.get(positions.get(0)).hisCashRemaining=playerState.get(positions.get(1)).myCashRemaining;
								playerState.get(positions.get(1)).hisCashRemaining=playerState.get(positions.get(0)).myCashRemaining;
							} else {
								playerState.get(positions.get(winner)).myCashRemaining+=potMap.get(positions.get(0));
								playerState.get(positions.get(winner)).myCashRemaining+=potMap.get(positions.get(1));
								playerState.get(positions.get(1-winner)).hisCashRemaining=playerState.get(positions.get(winner)).myCashRemaining;
							}
						}
						
						// Add this game to the previous games.
						playerState.get(positions.get(0)).previous.addGame(playerState.get(positions.get(0)).myDropCard, playerState.get(positions.get(0)).hisDropCard, playerState.get(positions.get(0)).myPlayCard, ((play==-1)?-1:playerState.get(positions.get(1)).myPlayCard), true, playerState.get(positions.get(0)).playsThusFar);
						playerState.get(positions.get(1)).previous.addGame(playerState.get(positions.get(1)).myDropCard, playerState.get(positions.get(1)).hisDropCard, playerState.get(positions.get(1)).myPlayCard, ((play==-1)?-1:playerState.get(positions.get(0)).myPlayCard), false, playerState.get(positions.get(1)).playsThusFar);
							
						// update the player states						
						System.out.println("Cash remaining: "+id[player1]+"->"+playerState.get(id[player1]).myCashRemaining+", "+id[player2]+"->"+playerState.get(id[player2]).myCashRemaining);
						logw.write("Cash remaining: "+id[player1]+"->"+playerState.get(id[player1]).myCashRemaining+", "+id[player2]+"->"+playerState.get(id[player2]).myCashRemaining); logw.newLine();
						logw.write("---"); logw.newLine();
					}
					// update the scores
					scores.put(id[player1], scores.get(id[player1])+playerState.get(id[player1]).myCashRemaining);
					scores.put(id[player2], scores.get(id[player2])+playerState.get(id[player2]).myCashRemaining);				
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
