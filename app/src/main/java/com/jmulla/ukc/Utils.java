package com.jmulla.ukc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

  static double metresToYards(double metres) {
    return metres * 1.09361;
  }

  static double yardsToMetres(double yards) {
    return yards * 0.9144;
  }

  static double gramsToOunces(double grams) {
    return grams * 0.035274;
  }

  static double ouncesToGrams(double ounces) {
    return ounces * 28.3495;
  }

  static double roundToDP(double value, int places){
    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
  }
}
