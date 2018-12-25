package com.jmulla.ukc;

import android.support.v4.util.Pair;

class Conversions {

  /*
   * patternYarn, coneYardage must be in metres
   * coneWeight must be in grams
   */
  static Pair<Double, Double> calculateAmounts(double patternYarn, double ballWeight, double ballYardage) {
    if (patternYarn <= 0 || ballWeight <= 0 || ballYardage <= 0){
      return null;
    }
    double weight = patternYarn * ballWeight/ballYardage;
    double numBalls = (weight / ballWeight);
    return new Pair<>(weight, numBalls);
  }


}
