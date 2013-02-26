package games.prisoners;

import java.util.Random;

public class Strategies {

    public class ResultClass {

        boolean valid = false;
        boolean result;
    }

    private class Player1111 {

        public Boolean play(PreviousGames games) {
            // Cooperate if this is the first move.
            // Otherwise, count the number of times that cooperation has occurred during previous play.
            // If cooperation has occurred at least as much as defection, then cooperate.
            if (games.myPreviousMoves.size() == 0) {
                return true;
            }
            int totalTrue = 0;
            for (int i = 0; i < games.myPreviousMoves.size(); i++) {
                totalTrue += (games.myPreviousMoves.get(i) ? 1 : 0);
            }
            for (int i = 0; i < games.hisPreviousMoves.size(); i++) {
                totalTrue += (games.hisPreviousMoves.get(i) ? 1 : 0);
            }
            return (totalTrue >= games.myPreviousMoves.size());
        }
    }

        private class Player5396 {

            public Boolean play(PreviousGames games) {

                if (games.hisPreviousMoves.size() <= 5) {
                    return true;
                }
                int n = (games.hisPreviousMoves.size() <= 100) ? games.hisPreviousMoves.size() : 100;
                double ht_given_mf = 0.0;
                double mt_given_hf = 0.0;
                double ht = 0.0;
                double hf = 0.0;
                double mf = 0.0;
                for(int i = games.hisPreviousMoves.size()-n+1;i <= games.hisPreviousMoves.size()-1;i++) {
                    if(games.hisPreviousMoves.get(i) == true) {
                        ht++;
                        if(games.myPreviousMoves.get(i) == false) {
                            ht_given_mf++;
                        }
                    } else {
                        hf++;
                    }
                    if(games.myPreviousMoves.get(i) == false) {
                        mf++;
                        if(games.hisPreviousMoves.get(i) == true) {
                            mt_given_hf++;
                        }
                    }
                }

                if(mf == 0){
                    System.out.println(" mf-0-false");
                    return false;
                }

                double ratio = (double)(ht / (n-1));
                double p_ratio = ht_given_mf / mf;
                double f_ratio = mt_given_hf / hf;
                System.out.print("n: " + n + " ht: " + ht + " hf: " + hf + " ht_given_mf: " + ht_given_mf + " mf: " + mf + " ratio: " + ratio
                        + " ratio check: " + (ratio > (.80) || ratio < (0.125)) + " p_ratio: " + p_ratio);
                if(ratio > (0.80) || ratio < (0.10)) {
                    System.out.println(" rc-false ");
                    return false;
                }
                java.util.Random r = new Random();




                double rr = r.nextDouble();
                System.out.println(" p_ratio: " + p_ratio + " rr:" + rr + " p_ratio check: " + (p_ratio < rr));

                return (f_ratio < (p_ratio - 0.25)) ? true : false;
            }
        }

    
    private class Player5555 {

        private double TRIGGER = 0.75;
        private int EXPECTED_ROUNDS = 5000;
        private double curve = 0;
        private double his_curve = 0;
        private Random gen = new Random();

        public Boolean play(PreviousGames games) {

            if (games.hisPreviousMoves.size() <= 10) {
                return true;
            }
            int coop = 0;
            int me = 0;
            int coop_recent = 0;
            int me_recent = 0;
            for (Boolean m : games.hisPreviousMoves) {
                coop += (m ? 1 : 0);
            }
            for (Boolean c : games.myPreviousMoves) {
                me += (c ? 1 : 0);
            }
            for (int i = games.myPreviousMoves.size() - 9; i < games.myPreviousMoves.size(); i++) {
                me_recent += (games.myPreviousMoves.get(i) ? 1 : 0);
            }
            for (int i = games.hisPreviousMoves.size() - 9; i < games.hisPreviousMoves.size(); i++) {
                coop_recent += (games.hisPreviousMoves.get(i) ? 1 : 0);
            }
            curve = (1 - TRIGGER) / java.lang.Math.log1p(EXPECTED_ROUNDS);
            his_curve = (1 - ((double) coop / games.hisPreviousMoves.size())) / games.hisPreviousMoves.size();

            curve = (curve > his_curve ? curve : his_curve);

            if (((double) coop / games.hisPreviousMoves.size()) >= TRIGGER || ((double) coop_recent / 10) >= (TRIGGER)) {


                if (gen.nextDouble() <= (curve * java.lang.Math.log1p(games.myPreviousMoves.size())) && ((double) me_recent / 10) >= (TRIGGER)) {

                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private class Player5397 {
        //5397 does not check other's curve and has more conservative check against my recent

        private double TRIGGER = 0.25;
        private int EXPECTED_ROUNDS = 1000;
        private double curve = 0;
        private double his_curve = 0;
        private Random gen = new Random();

        public Boolean play(PreviousGames games) {

            if (games.hisPreviousMoves.size() <= 10) {
                System.out.println("Size is less than 10. returning true.");
                return true;
            }
            int coop = 0;
            int me = 0;
            int coop_recent = 0;
            int me_recent = 0;
            for (Boolean m : games.hisPreviousMoves) {
                coop += (m ? 1 : 0);
            }
            for (Boolean c : games.myPreviousMoves) {
                me += (c ? 1 : 0);
            }
            for (int i = games.myPreviousMoves.size() - 9; i < games.myPreviousMoves.size(); i++) {
                me_recent += (games.myPreviousMoves.get(i) ? 1 : 0);
            }
            for (int i = games.hisPreviousMoves.size() - 9; i < games.hisPreviousMoves.size(); i++) {
                coop_recent += (games.hisPreviousMoves.get(i) ? 1 : 0);
            }
            curve = (1 - TRIGGER) / java.lang.Math.log1p(EXPECTED_ROUNDS);
            his_curve = ((double) coop / games.hisPreviousMoves.size()) / games.hisPreviousMoves.size();
            System.out.print(" His plays: " + ((double) coop / games.hisPreviousMoves.size()));
            System.out.print(" My plays: " + ((double) me / games.myPreviousMoves.size()));
            if (((double) coop / games.hisPreviousMoves.size()) >= TRIGGER || ((double) coop_recent / 10) >= (TRIGGER)) {

                if (gen.nextDouble() <= (curve * java.lang.Math.log1p(games.myPreviousMoves.size())) && ((double) me_recent / 10) >= (TRIGGER - 0.1)) {
                    System.out.println(" F");
                    return false;
                } else {
                    System.out.println(" T");
                    return true;
                }
            } else {
                System.out.println("FF");
                return false;
            }
        }
    }

    private class Player5398 {

            public Boolean play(PreviousGames games) {

                if (games.hisPreviousMoves.size() <= 5) {
                    return true;
                }
                int n = (games.hisPreviousMoves.size() <= 100) ? games.hisPreviousMoves.size() : 100;
                double ht_given_mf = 0.0;
                double mt_given_hf = 0.0;
                double ht = 0.0;
                double hf = 0.0;
                double mf = 0.0;
                for(int i = games.hisPreviousMoves.size()-n+1;i <= games.hisPreviousMoves.size()-1;i++) {
                    if(games.hisPreviousMoves.get(i) == true) {
                        ht++;
                        if(games.myPreviousMoves.get(i) == false) {
                            ht_given_mf++;
                        }
                    } else {
                        hf++;
                    }
                    if(games.myPreviousMoves.get(i) == false) {
                        mf++;
                        if(games.hisPreviousMoves.get(i) == true) {
                            mt_given_hf++;
                        }
                    }
                }

                if(mf == 0){
                    System.out.println(" mf-0-false");
                    return false;
                }

                double ratio = (double)(ht / (n-1));
                double p_ratio = ht_given_mf / mf;
                double f_ratio = mt_given_hf / hf;
                System.out.print("n: " + n + " ht: " + ht + " hf: " + hf + " ht_given_mf: " + ht_given_mf + " mf: " + mf + " ratio: " + ratio
                        + " ratio check: " + (ratio > (.80) || ratio < (0.125)) + " p_ratio: " + p_ratio);
                if(ratio > (0.80) || ratio < (0.10)) {
                    System.out.println(" rc-false ");
                    return false;
                }
                java.util.Random r = new Random();

                double rr = r.nextDouble();
                System.out.println(" p_ratio: " + p_ratio + " rr:" + rr + " p_ratio check: " + (p_ratio < rr));

                return (f_ratio < (p_ratio - 0.25 * rr)) ? true : false;
            }
        }

    private class Player2222 {

        public Boolean play(PreviousGames games) {
            return (new Random().nextBoolean());
        }
    }

    private class Player4444 {

        public Boolean play(PreviousGames games) {
            return true;
        }
    }

    private class Player3333 {

        public Boolean play(PreviousGames games) {
            return false;
        }
    }

    // TODO: You should write a class for PlayerXXXX (where XXXX is the last 4 digits of your student ID) and
    // execute this code. You will only turn in a file that consists of a player class that you write yourself.
    public class PlayerThread extends Thread {

        public ResultClass result;
        private PreviousGames games;
        private Integer id;

        public PlayerThread(PreviousGames g, ResultClass r, int identification) {
            games = g;
            result = r;
            id = identification;
        }

        public void run() {
            try {
                switch (id) {
                    case 1111:
                        result.result = new Player1111().play(games);
                        break;
                    case 2222:
                        result.result = new Player2222().play(games);
                        break;
                    case 3333:
                        result.result = new Player3333().play(games);
                        break;
                    case 4444:
                        result.result = new Player4444().play(games);
                        break;
                    case 5396:
                        result.result = new Player5396().play(games);
                        break;
                    case 5397:
                        result.result = new Player5397().play(games);
                        break;
                    case 5398:
                        result.result = new Player5398().play(games);
                        break;
                    case 5555:
                        result.result = new Player5555().play(games);
                        break;
                    default:
                        throw new RuntimeException("Unsupported player.");
                }
                result.valid = true;
            } catch (Exception e) {
                System.out.println(e);
                result.valid = false;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public Boolean getResult(int id, PreviousGames games) {
        ResultClass result = new ResultClass();
        PlayerThread thread = new PlayerThread(games, result, id);
        thread.start();
        for (int iterations = 0; iterations < 3000; iterations++) { // 3 second maximum
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (result.valid) {
                return result.result;
            }
        }
        thread.stop();
        return null;
    }
}