private class Player5396 {
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
            int coop = 0; int me = 0; int coop_recent = 0; int me_recent = 0;
            for (Boolean m : games.hisPreviousMoves) {
                coop += (m ? 1 : 0);
            }
            for (Boolean c : games.myPreviousMoves) {
                me += (c ? 1:0);
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
            if (((double) coop / games.hisPreviousMoves.size()) >= TRIGGER) {
                
                
                if (gen.nextDouble() <= (curve * java.lang.Math.log1p(games.myPreviousMoves.size()))) {
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