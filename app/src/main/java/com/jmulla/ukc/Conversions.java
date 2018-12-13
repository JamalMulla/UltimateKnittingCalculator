package com.jmulla.ukc;

import android.support.v4.util.Pair;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Conversions {

  /*
   * patternYarn, coneYardage must be in metres
   * coneWeight must be in grams
   */
  static Pair<Double, Double> calculateAmounts(double patternYarn, double ballWeight, double ballYardage) {
    double weight = roundToDP(patternYarn * ballWeight/ballYardage, 2);
    double numBalls = roundToDP(weight / ballWeight, 2);
    return new Pair<>(weight, numBalls);
  }



  private static double roundToDP(double value, int places){
    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
  }
}
