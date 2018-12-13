package com.jmulla.ukc;

import static com.jmulla.ukc.Conversions.calculateAmounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConversionActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_convert);

    final EditText et_yardage = findViewById(R.id.et_yardage);
    final EditText et_skein_weight = findViewById(R.id.et_skein_weight);
    final EditText et_skein_yardage = findViewById(R.id.et_skein_yardage);
    Button btn_convert = findViewById(R.id.btn_convert);
    final TextView tv_yarn_weight = findViewById(R.id.tv_yarn_weight);
    final TextView tv_num_balls = findViewById(R.id.tv_num_balls);

    btn_convert.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        double patternYarn = Double.valueOf(et_yardage.getText().toString());
        double ballWeight = Double.valueOf(et_skein_weight.getText().toString());
        double ballYardage = Double.valueOf(et_skein_yardage.getText().toString());
        Pair<Double, Double> doubleDoublePair = calculateAmounts(patternYarn, ballWeight,
            ballYardage);
        tv_yarn_weight.setText(String.valueOf(doubleDoublePair.first));
        tv_num_balls.setText(String.valueOf(doubleDoublePair.second));
      }
    });


  }
}
