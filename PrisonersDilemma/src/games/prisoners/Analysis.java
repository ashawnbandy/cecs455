/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.prisoners;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author shawn
 */
public class Analysis {

    public class GameStatistics {

        java.util.Map<Integer, java.util.ArrayList<Boolean>> game;
        Integer observations;
        double[][][] p1_conditionals = new double[2][2][2];
        double[][][] p2_conditionals = new double[2][2][2];

    }
    //private java.util.Map<Integer, java.util.ArrayList<Boolean>> playerMoves = new java.util.HashMap<Integer, java.util.ArrayList<Boolean>>();
    private java.util.ArrayList<java.util.Map<Integer, java.util.ArrayList<Boolean>>> games = new java.util.ArrayList<java.util.Map<Integer, java.util.ArrayList<Boolean>>>();
    private ArrayList<GameStatistics> stats = new ArrayList<GameStatistics>();
    AggregateSummaryStatistics aggregate = new AggregateSummaryStatistics();

    public Analysis(String scoreFilename) {
        int position = 0;
        int currentlinenumber = 0;
        String currentLine = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(scoreFilename));
            currentLine = "";
            currentlinenumber = 0;
            java.util.Map<Integer, java.util.ArrayList<Boolean>> game = new java.util.HashMap<Integer, java.util.ArrayList<Boolean>>();
            while ((currentLine = br.readLine()) != null) {
                currentlinenumber++;

                if (currentLine.charAt(0) == '-') {
                    games.add(game);
                    game = new java.util.HashMap<Integer, java.util.ArrayList<Boolean>>();
                } else {

                    int id = 1000 * (currentLine.charAt(0) - '0') + 100 * (currentLine.charAt(1) - '0') + 10 * (currentLine.charAt(2) - '0') + (currentLine.charAt(3) - '0');
                    position = 7;
                    ArrayList<Boolean> moves = new java.util.ArrayList<Boolean>();

                    while (position < currentLine.length()) {
                        switch (currentLine.charAt(position)) {
                            case 't':
                                moves.add(true);
                                position += 6;
                                break;
                            case 'f':
                                moves.add(false);
                                position += 7;
                                break;
                            default:
                                position += 1;
                                break;
                        }
                    }
                    game.put(id, moves);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(currentlinenumber + " " + currentLine.length() + " " + position + " " + games.size() + " " + e);
        }
    }

    public void calculateStats() {
        System.out.println("Total games(B4): " + games.size());
        for (Iterator<Map<Integer, java.util.ArrayList<Boolean>>> it = games.iterator(); it.hasNext();) {
            Map<Integer, java.util.ArrayList<Boolean>> game = it.next();
            if (game.size() != 2) {
                System.out.println("Game size (" + game.size() + ") is the wrong size.  Removing.");
                for (Integer i : game.keySet()) {
                    System.out.print(" " + i);
                }
                System.out.print("\n");
                it.remove();
            } else {
                GameStatistics gs = new GameStatistics();
                gs.game = game;
                Iterator<Integer> it2 = game.keySet().iterator();
                Integer p1 = it2.next();
                Integer p2 = it2.next();
                ArrayList<Boolean> p1_moves = gs.game.get(p1);
                ArrayList<Boolean> p2_moves = gs.game.get(p2);
//                if(p1_moves.size() != p2_moves.size()) {
//                    System.err.println("mismatched moves sizes");
//                    it.remove();
//                    break;
//                }
                assert (p1_moves.size() == p2_moves.size());
                gs.observations = p1_moves.size();
                System.out.print(p1 + " " + p2 + " " + p1_moves.size() + " " + p2_moves.size());
                double[] p1_moves_d = new double[p1_moves.size()];
                double[] p2_moves_d = new double[p1_moves.size()];
                SummaryStatistics p1_stats = aggregate.createContributingStatistics();
                SummaryStatistics p2_stats = aggregate.createContributingStatistics();
                double[][][] p1_conditional = new double[2][2][2];
                double[][][] p2_conditional = new double[2][2][2];
                double[][][] p1_conditional_count = new double[2][2][2];
                double[][][] p2_conditional_count = new double[2][2][2];
                for (int i = 0; i < p1_moves.size() - 1; i++) {
                    p1_moves_d[i] = (p1_moves.get(i)) ? 1.0 : 0.0;
                    p2_moves_d[i] = (p2_moves.get(i)) ? 1.0 : 0.0;
                    p1_stats.addValue((p1_moves.get(i)) ? 1.0 : 0.0);
                    p2_stats.addValue((p2_moves.get(i)) ? 1.0 : 0.0);
                    if (i >= 1) {
                        p1_conditional_count[((int) p1_moves_d[i])][((int) p1_moves_d[i - 1])][((int) p2_moves_d[i - 1])] = (p1_moves.get(i)) ? 1.0 : 0.0;
                        p2_conditional_count[((int) p1_moves_d[i])][((int) p1_moves_d[i - 1])][((int) p2_moves_d[i - 1])] = (p1_moves.get(i)) ? 1.0 : 0.0;
                        p1_conditional[((int) p1_moves_d[i])][((int) p1_moves_d[i - 1])][((int) p2_moves_d[i - 1])]++;
                        p2_conditional[((int) p1_moves_d[i])][((int) p1_moves_d[i - 1])][((int) p2_moves_d[i - 1])]++;
                    }
                }
                System.out.print(" " + p1_stats.getMean() + " " + p2_stats.getMean());
                for (int j = 0; j <= 1; j++) {
                    for (int k = 0; k <= 1; k++) {
                        for (int l = 0; l <= 1; l++) {
                            gs.p1_conditionals[j][k][k] = (p1_conditional_count[j][k][l] / p1_conditional[j][k][l]);
                         //   System.out.print(" " + j + k + l + " " + (p1_conditional_count[j][k][l] / p1_conditional[j][k][l]));
                        }
                    }
                }
                try {
                double cov = new Covariance().covariance(p1_moves_d, p2_moves_d);
                System.out.print(" " + cov);
                SimpleRegression p1_sr = new SimpleRegression();
                SimpleRegression p2_sr = new SimpleRegression();

                System.out.print("\n");
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
        System.out.println("Total games: " + games.size());
        System.out.println("Pop mean: " + aggregate.getMean());

    }
}
