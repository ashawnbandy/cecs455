/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.prisoners;

/**
 *
 * @author shawn
 */
public class Main {
    public static void main(String[] args) {
//        Analysis a = new Analysis("/Users/Shawn/Downloads/Info/prisonerLog.txt");
//        a.calculateStats();
        PlayGame p = new PlayGame();
        for (int i = 1; i <= 30; i++) {
            p.runGame("score.log", "log.log");
        }

    }
}
