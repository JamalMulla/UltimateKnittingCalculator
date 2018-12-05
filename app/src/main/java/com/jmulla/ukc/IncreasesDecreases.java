package com.jmulla.ukc;

import android.util.Pair;

public class IncreasesDecreases {



    private static String knitBet2_1Inner;
    private static String knitBet2_2Inner;
    private static String kntiBet2_3Inner;

    /*
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        while (true){
            String input = scanner.nextLine();
            switch (input) {
                case "q":
                    scanner.close();
                    return;
                case "i": {
                    int curStitches = scanner.nextInt();
                    int inc = scanner.nextInt();
                    increase(curStitches, inc);
                    continue;
                }
                case "d": {
                    int curStitches = scanner.nextInt();
                    int dec = scanner.nextInt();
                    decrease(curStitches, dec);
                }
            }
        }

    }





     */


    static Pair<String, String> increase(int stitches, int incStitches) {
        if (stitches <= 0 || incStitches <= 0){
            return new Pair<>("", "");
        }
        double times1_1 = Math.min(Math.floor(stitches - 1), incStitches);
        // calculate first
        double stitches_1 = (double) stitches;

        double knitBet1 = Math.floor(stitches_1 / times1_1) - 1;

        double knitEnd1 = stitches_1 - (knitBet1 + 1) * times1_1;


        // calculate second

        double highTimes = stitches_1 % times1_1;
        double lowTimes = times1_1 - highTimes;

        double nonRoundBet = stitches_1 / times1_1;
        double lowBet = Math.floor(nonRoundBet);
        double highBet = Math.ceil(nonRoundBet);


        double times2_2;
        double knitBet2_2;
        double times2_1;
        double knitBet2_1;
        double times2_3;
        double knitBet2_3;
        double knitEnd2;
        double knitBeg2;

        if (highTimes % 2 == 1) {
            times2_2 = highTimes;
            knitBet2_2 = highBet;
            times2_1 = Math.ceil((lowTimes - 1) / 2);
            knitBet2_1 = lowBet;
            times2_3 = Math.floor((lowTimes - 1) / 2);
            knitBet2_3 = lowBet;
            knitEnd2 = Math.floor(lowBet / 2);
            knitBeg2 = Math.ceil(lowBet / 2);
        } else {
            times2_2 = lowTimes - 1;
            knitBet2_2 = lowBet;
            times2_1 = Math.ceil(highTimes / 2);
            knitBet2_1 = highBet;
            times2_3 = Math.floor(highTimes / 2);
            knitBet2_3 = highBet;
            knitEnd2 = Math.floor(lowBet / 2);
            knitBeg2 = Math.ceil(lowBet / 2);
        }

        String m1S = cleanInner((int) times1_1, cleanInner((int) knitBet1, "(K%d, M1 K1) ", "(M1 K1) ") + "%d times", "");
        //System.out.println(knitEnd1);
        String end = cleanInner((int) knitEnd1, ", end with K%d", "");
        String method1 = m1S + end;

        knitBet2_1Inner = cleanInner((int) knitBet2_1, "(M1, K%d) ", "k2tog ");
        knitBet2_2Inner = cleanInner((int) knitBet2_2, "(M1, K%d) ", "k2tog ");
        kntiBet2_3Inner = cleanInner((int) knitBet2_3, "(M1, K%d) ", "k2tog ");
        String method2 =
                cleanInner((int) knitEnd2, "K%d, ", "") +
                        cleanInner((int) times2_1, knitBet2_1Inner + "%d times, ", "") +
                        cleanInner((int) times2_2, knitBet2_2Inner + "%d times, ", "") +
                        cleanInner((int) times2_3, kntiBet2_3Inner + "%d times, ", "") +
                        cleanInner((int) knitBeg2, "M1, K%d ", "M1");

        return new Pair<>(method1, method2);
        //p("Method 1 - (unbalanced): " + method1);
        //p("Method 2 - (more balanced): " + method2);

    }

    static Pair<String, String> decrease(int stitches, int decStitches) {
        if (stitches <= 0 || decStitches <= 0){
            return new Pair<>("", "");
        }
        double times1_1 = Math.min(Math.floor(stitches / 2), decStitches);
        // calculate first
        //p.times1_1.value=times1_1;
        double knitBet1 = Math.floor((stitches - 2 * times1_1) / times1_1);
        //p.knitBet1.value=knitBet1;
        double knitEnd1 = stitches - (knitBet1 + 2) * times1_1;
        //p.knitEnd1.value=knitEnd1;

        // calculate second

        double highTimes = (stitches - 2 * times1_1) % times1_1;
        double lowTimes = times1_1 - highTimes;

        double nonRoundBet = (stitches - 2 * times1_1) / times1_1;
        double lowBet = Math.floor(nonRoundBet);
        double highBet = Math.ceil(nonRoundBet);


        double times2_2;
        double knitBet2_2;
        double times2_1;
        double knitBet2_1;
        double times2_3;
        double knitBet2_3;
        double knitEnd2;
        double knitBeg2;

        if(highTimes%2==1){ // high repeated an odd number of times so in center.
            times2_2 = highTimes;
            knitBet2_2 = highBet;
            times2_1 = Math.ceil((lowTimes-1)/2);
            knitBet2_1 = lowBet;
            times2_3= Math.floor((lowTimes-1)/2);
            knitBet2_3 = lowBet;
            knitEnd2 = Math.floor(lowBet/2);
            knitBeg2 = Math.ceil(lowBet/2);
        }
        else {
            times2_2 = lowTimes - 1;
            knitBet2_2 = lowBet;
            times2_1 = Math.ceil(highTimes/2);
            knitBet2_1 = highBet;
            times2_3=  Math.floor(highTimes/2);
            knitBet2_3 = highBet;
            knitEnd2 = Math.floor(lowBet/2);
            knitBeg2 = Math.ceil(lowBet/2);
        }

        String m1S = cleanInner((int) knitBet1,  "(K%d, k2tog) ", "k2tog ");
        String end = cleanInner((int) knitEnd1, ", end with K%d", "");
        String method1 = m1S + (int) times1_1 + " times" + end;

        //p("Method 1 - (unbalanced): " + method1);

        knitBet2_1Inner = cleanInner((int) knitBet2_1, "(k2tog, K%d) ", "k2tog ");
        knitBet2_2Inner = cleanInner((int) knitBet2_2, "(k2tog, K%d) ", "k2tog ");
        kntiBet2_3Inner = cleanInner((int) knitBet2_3, "(k2tog, K%d) ", "k2tog ");
        String method2 =
                cleanInner((int) knitBeg2, "K%d, ", "") +
                        cleanInner((int) times2_1, knitBet2_1Inner + "%d times, ", "") +
                        cleanInner((int) times2_2, knitBet2_2Inner + "%d times, ", "") +
                        cleanInner((int) times2_3, kntiBet2_3Inner + "%d times, ", "") +
                        cleanInner((int) knitEnd2, "k2tog, K%d", "k2tog");

        return new Pair<>(method1, method2);
        //p("Method 2 - (more balanced): " + method2);
    }



    private static String cleanInner(int val, String option1, String option2){
        if (val > 0){
            return String.format(option1, val);
        } else {
            return option2;
        }
    }

}
