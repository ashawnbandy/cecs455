package games.restaurant;

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

	private int chooseNumRestaurants() {
		if (id.length<=4) return 2;
		int x=id.length/(new Random().nextInt(3)+2);
		if (x<2) return 2;
		return x;
	}
	private ArrayList<ArrayList<Integer>> chooseRatings(int numRestaurants) {
		ArrayList<ArrayList<Integer>> ratings=new ArrayList<ArrayList<Integer>>();
		// The experience will improve by a random integer between 0 and 3 starting from 0.
		Random random=new Random();
		for (int i=0; i<numRestaurants; i++) {
			ArrayList<Integer> current=new ArrayList<Integer>();
			current.add(0);
			for (int j=0; j<id.length-1; j++)
				current.add(current.get(current.size()-1)+random.nextInt(4));
			ratings.add(current); }
		return ratings;
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

			
			// The number of restaurants is equal to the number of players divided by either 2, 3, or 4.
			PreviousGames games=new PreviousGames();
			for (int iteration=0; iteration<300; iteration++) {
				int numRestaurants=chooseNumRestaurants();
				ArrayList<ArrayList<Integer>> ratings=chooseRatings(numRestaurants);
				System.out.print(ratings.toString()+": ");
				ArrayList<Integer> choices=new ArrayList<Integer>();
				for (int i=0; i<id.length; i++) {
					Integer choice=new Strategies().getResult(id[i], games, ratings);
					if ((choice==null)||(choice<0)||(choice>=ratings.size())) {
						System.out.println("There was invalid play by "+id[i]+"."); choice=-1; }
					choices.add(choice); }
				System.out.println(choices.toString());
				
				// compute the scores
				TreeMap<Integer,Integer> choiceMap=new TreeMap<Integer,Integer>();
				for (int i=0; i<id.length; i++) {
					choiceMap.put(id[i], choices.get(i));
					if (choices.get(i)==-1) continue;
					int total=0;
					for (int j=0; j<id.length; j++)
						if (choices.get(i)==choices.get(j))
							total++;
					if (!scores.containsKey(id[i])) throw new RuntimeException("Score not found.");
					int s=ratings.get(choices.get(i)).size();
					scores.put(id[i], scores.get(id[i])+ratings.get(choices.get(i)).get(s-total)); }
				logw.write(ratings.toString()); logw.newLine();
				logw.write(choiceMap.toString()); logw.newLine();
				logw.write("---"); logw.newLine();
				games.addGame(ratings, choiceMap); }
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
