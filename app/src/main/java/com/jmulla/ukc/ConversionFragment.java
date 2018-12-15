package com.jmulla.ukc;

import static com.jmulla.ukc.Conversions.calculateAmounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConversionFragment extends Fragment {


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_convert, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    FragmentActivity activity = getActivity();
    final EditText et_yardage = activity.findViewById(R.id.et_yardage);
    final EditText et_skein_weight = activity.findViewById(R.id.et_skein_weight);
    final EditText et_skein_yardage = activity.findViewById(R.id.et_skein_yardage);
    Button btn_convert = activity.findViewById(R.id.btn_convert);
    final TextView tv_yarn_weight = activity.findViewById(R.id.tv_yarn_weight);
    final TextView tv_num_balls = activity.findViewById(R.id.tv_num_balls);

    btn_convert.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        double patternYarn;
        double ballWeight;
        double ballYardage;
        try {
          patternYarn = Double.valueOf(et_yardage.getText().toString());
        } catch (NumberFormatException e) {
          return;
        }
        try {
          ballWeight = Double.valueOf(et_skein_weight.getText().toString());
        } catch (NumberFormatException e) {
          return;
        }
        try {
          ballYardage = Double.valueOf(et_skein_yardage.getText().toString());
        } catch (NumberFormatException e) {
          return;
        }
        Pair<Double, Double> doubleDoublePair = calculateAmounts(patternYarn, ballWeight,
            ballYardage);
        if (doubleDoublePair == null) {
          System.out.println("Invalid values\n");
        } else {
          String yarn_weight = String.valueOf(doubleDoublePair.first);
          tv_yarn_weight.setText(String.format("You will need %s grams of yarn.", yarn_weight));
          String num_balls = String.valueOf(doubleDoublePair.second);
          tv_num_balls.setText(String
              .format("This is %s skeins (cones, balls). Best to round up when buying", num_balls));
        }
      }
    });


  }
}
