package games.prisoners;

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
	public int[] id={1111,2222,3333,4444,5555,5396,5397, 5398};
	
	private int score(boolean me, boolean you) {
		if (me&&you) return 4;
		if (me&&!you) return 0;
		if (!me&&you) return 7;
		if (!me&&!you) return 1;
		throw new RuntimeException("This should never happen.");
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
			ArrayList<Boolean> iPlays=new ArrayList<Boolean>();
			ArrayList<Boolean> jPlays=new ArrayList<Boolean>();
			for (int i=0; i<id.length; i++)
				for (int j=i+1; j<id.length; j++) {
					if (!scores.containsKey(id[i])) throw new RuntimeException("Player has no score.");
					if (!scores.containsKey(id[j])) throw new RuntimeException("Player has no score.");
					System.out.print("Player "+id[i]+" versus Player "+id[j]+"["+scores.get(id[i])+","+scores.get(id[j])+"]... ");
					TreeMap<Integer,PreviousGames> gamesMap=new TreeMap<Integer,PreviousGames>();
					gamesMap.put(id[i], new PreviousGames());
					gamesMap.put(id[j], new PreviousGames());
					boolean done=false;
					while (!done) {
						PreviousGames games=gamesMap.get(id[i]);
						games.hisPreviousMoves=jPlays;
						games.myPreviousMoves=iPlays;
						Boolean iCurrent=new Strategies().getResult(id[i], games);
						if (iCurrent==null) {
							System.out.println("There was invalid play by "+id[i]+".");
							logw.write("There was invalid play by "+id[i]+"."); logw.newLine();
							done=true; continue; }
						// There is 3% probability that you make a playing mistake.
						int r=random.nextInt(100);
						if ((r==1)||(r==2)||(r==3)) iCurrent=!iCurrent;
						games=gamesMap.get(id[j]);
						games.hisPreviousMoves=iPlays;
						games.myPreviousMoves=jPlays;
						Boolean jCurrent=new Strategies().getResult(id[j], games);
						if (jCurrent==null) {
							System.out.println("There was invalid play by "+id[j]+".");
							logw.write("There was invalid play by "+id[j]+"."); logw.newLine();
							done=true; continue; }
						// 3% probability of mistake
						r=random.nextInt(100);
						if ((r==1)||(r==2)||(r==3)) jCurrent=!jCurrent;
						iPlays.add(iCurrent); jPlays.add(jCurrent);
						
						// update score
						scores.put(id[i], scores.get(id[i])+score(iCurrent,jCurrent));
						scores.put(id[j], scores.get(id[j])+score(jCurrent,iCurrent));
						done=(random.nextInt(1000)==1);
					}
					System.out.println("["+scores.get(id[i])+","+scores.get(id[j])+"] done.");
					logw.write(""+id[i]+": "+iPlays); logw.newLine();
					logw.write(""+id[j]+": "+jPlays); logw.newLine();
					logw.write("---"); logw.newLine();
					
					iPlays.clear(); jPlays.clear();
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
