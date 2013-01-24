package games.poker;

import java.util.Random;

public class Strategies {
	public class ResultClass {
		boolean valid=false; Integer result; }
	public class InitialResultClass {
		boolean valid=false; Boolean result; }
	
	private class Player1111 {
		public Boolean initialPlay(PlayerGameState state) {
			return (state.cards.get(0)>=state.cards.get(1));
			// This will choose the 0 card if it is larger than the 1 card.
		}
		public Integer play(PlayerGameState state) {
			if (state.playsThusFar.size()<=400)
				return 0; else return 1;
			// If there have been fewer than 400 moves thus far in the game, this player will pass.
			// Otherwise, he will call.
		}
	}
	
	// TODO: Create Players 2222 and 3333 exactly as above.
	
	public class InitialThread extends Thread {
		private PlayerGameState state; private InitialResultClass result; private int id;
		public InitialThread(PlayerGameState s, InitialResultClass r, int identification) { state=s; result=r; id=identification; }
		public void run() {
			switch (id) {
			case 1111: result.result=new Player1111().initialPlay(state); break;
			case 2222: result.result=new Player2222().initialPlay(state); break;
			case 3333: result.result=new Player3333().initialPlay(state); break;
			default: throw new RuntimeException("Unsupported player.");	}
			result.valid=true;
		}
	}
	public class PlayThread extends Thread {
		private PlayerGameState state; private ResultClass result; private int id;
		public PlayThread(PlayerGameState s, ResultClass r, int identification) { state=s; result=r; id=identification; }
		public void run() {
			switch (id) {
			case 1111: result.result=new Player1111().play(state); break;
			case 2222: result.result=new Player2222().play(state); break;
			case 3333: result.result=new Player3333().play(state); break;
			default: throw new RuntimeException("Unsupported player.");	}
			result.valid=true;
		}		
	}
	
	@SuppressWarnings("deprecation")
	public Boolean getInitialResult(int id, PlayerGameState state) {
		InitialResultClass result=new InitialResultClass();
		InitialThread thread=new InitialThread(state,result,id);
		thread.start();
		// Let there be a 3 second maximum.
		for (int iterations=0; iterations<30; iterations++) {
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();	}
			if (result.valid) return result.result; }
		thread.stop();
		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	public Integer getResult(int id, PlayerGameState state) {
		ResultClass result=new ResultClass();
		PlayThread thread=new PlayThread(state,result,id);
		thread.start();
		// Let there be a 3 second maximum.
		for (int iterations=0; iterations<30; iterations++) {
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();	}
			if (result.valid) return result.result; }
		thread.stop();
		return null;
	}
}
