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
