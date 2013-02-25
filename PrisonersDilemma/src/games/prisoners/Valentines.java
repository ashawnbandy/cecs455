/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.prisoners;

/**
 *
 * @author shawn
 */
public class Valentines {

    public static class State {
        public String stateName;
        public Double start_probability;
        public java.util.Map<State, Double> transition_probabilities;
        public java.util.Map<Boolean, Double> emission_probabilities;
    }

    public Valentines(java.util.ArrayList<Boolean> observations) {
        //default states for prisoners dilemma
        State cooperate = new State();
        cooperate.start_probability = 0.99;
        cooperate.emission_probabilities.put(true,0.97);
        cooperate.emission_probabilities.put(false,0.03);
        
        State retaliate = new State();
        retaliate.start_probability = 0.01;
        retaliate.emission_probabilities.put(true,0.03);
        retaliate.emission_probabilities.put(false,0.97);
        
        State test = new State();
        test.start_probability = 0.0;
        test.emission_probabilities.put(true,0.6);
        test.emission_probabilities.put(false,0.4);
        
        State apologize = new State();
        apologize = new State();
        apologize.emission_probabilities.put(true,0.4);
        apologize.emission_probabilities.put(false,0.6);
        
        
        
    }
    public State[] findPath(java.util.ArrayList<Boolean> observations,
            java.util.ArrayList<State> states) {

        Double V[][] = new Double[observations.size()][states.size()];
        State Path[][] = new State[observations.size()][states.size()];

        //initialize for t=0
        for (State s : states) {
            V[states.indexOf(s)][0] = s.start_probability * s.emission_probabilities.get(observations.get(0));
            Path[states.indexOf(s)][0] = s;
        }

        for (int t = 1; t <= observations.size() - 1; t++) {
            double maxProbability = 0;
            double p = 0;
            for (State s : states) {
                State maxState = null;
                for (State ss : states) {
                    p = V[states.indexOf(ss)][t-1] * ss.transition_probabilities.get(s) * s.emission_probabilities.get(observations.get(t));
                    if (p >= maxProbability) {
                        maxProbability = p;
                        maxState = ss;
                    }
                }
                V[states.indexOf(s)][t] = maxProbability;
                Path[states.indexOf(s)][t] = maxState;
            }
        }
        double maxProbability = 0;
        State maxState = null;
        for (State s : states) {

            if (V[states.indexOf(s)][observations.size() - 1] >= maxProbability) {
                maxProbability = V[states.indexOf(s)][observations.size() - 1];
                maxState = s;
            }
        }

        return Path[states.indexOf(maxState)];
    }
}
