package com.jmulla.ukc;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Utils {

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
    return new BigDecimal(value).setScale(places, RoundingMode.HALF_EVEN).doubleValue();
  }

  static void applySpan(SpannableString spannable, String target, ClickableSpan span) {
    final String spannableString = spannable.toString();
    final int start = spannableString.indexOf(target);
    final int end = start + target.length();
    spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
  }
}
