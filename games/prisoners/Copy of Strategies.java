package games.prisoners;

import java.util.Iterator;
import java.util.Random;

public class Strategies {
	public class ResultClass {
		boolean valid=false; boolean result; }
	
	private class Player1111 {
		public Boolean play(PreviousGames games) {
			if (games.previous.firstEntry().getValue().size()==0) return true;
			int totalTrue=0;
			for (int i=0; i<games.previous.firstEntry().getValue().size(); i++) {
				Iterator<Integer> mapIter=games.previous.keySet().iterator();
				while (mapIter.hasNext()) {
					totalTrue+=((games.previous.get(mapIter.next()).get(i))?1:0); } }
			return (totalTrue*2>=games.previous.firstEntry().getValue().size()*games.previous.size());
		}
	}
	
	private class Player2222 {
		public Boolean play(PreviousGames games) {
			Random r=new Random();
			return r.nextBoolean();
		}
	}

	private class Player3333 {
		public Boolean play(PreviousGames games) {
			int otherID=-1;
			Iterator<Integer> playerIter=games.previous.keySet().iterator();
			while (playerIter.hasNext()) {
				int current=playerIter.next();
				if (current!=3333) { otherID=current; break; } }
			if (otherID==-1) return true;
			return games.previous.get(otherID).get(games.previous.get(otherID).size()-1);
		}
	}

	public class PlayerThread extends Thread {
		public ResultClass result; private PreviousGames games; private Integer id;
		public PlayerThread(PreviousGames g, ResultClass r, int identification) { games=g; result=r; id=identification; }
		public void run() {
			switch (id) {
			case 1111: result.result=new Player1111().play(games); result.valid=true; break;
			case 2222: result.result=new Player2222().play(games); result.valid=true; break;
			case 3333: result.result=new Player3333().play(games); result.valid=true; break;
			default: throw new RuntimeException("Unsupported player."); }
		}
	}
	@SuppressWarnings("deprecation")
	public Boolean getResult(int id, PreviousGames games) {
		ResultClass result=new ResultClass();
		PlayerThread thread=new PlayerThread(games,result,id);
		thread.start();
		for (int iterations=0; iterations<30; iterations++) { // 3 second maximum
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
			if (result.valid) return result.result; }
		thread.stop();
		return null;
	}
}
