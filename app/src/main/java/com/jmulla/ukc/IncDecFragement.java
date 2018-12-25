package com.jmulla.ukc;

import static com.jmulla.ukc.IncreasesDecreases.decrease;
import static com.jmulla.ukc.IncreasesDecreases.increase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class IncDecFragement extends Fragment {

  private RadioGroup switch_mode;
  private RadioButton btn_increase;
  private RadioButton btn_decrease;
  private TextInputEditText et_num_stitches;
  private TextInputEditText et_num_change;
  private TextInputLayout til;
  private TextView tv_method2;
  private TextView tv_method1;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_inc_dec, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    FragmentActivity activity = getActivity();
    switch_mode = activity.findViewById(R.id.switch_mode);
    btn_increase = activity.findViewById(R.id.btn_increase);
    btn_decrease = activity.findViewById(R.id.btn_decrease);
    Button btn_calc = activity.findViewById(R.id.btn_calc);
    Button btn_clear = activity.findViewById(R.id.btn_clear);
    et_num_stitches = activity.findViewById(R.id.et_num_stitches);
    et_num_change = activity.findViewById(R.id.et_num_change);
    til = activity.findViewById(R.id.til_inc_dec);
    tv_method1 = activity.findViewById(R.id.tv_method1);
    tv_method2 = activity.findViewById(R.id.tv_method2);

    btn_calc.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        calculateAndSetTVs();
      }
    });

    btn_clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        et_num_stitches.requestFocus();
        et_num_stitches.setText("");
        et_num_change.setText("");
        tv_method1.setText("");
        tv_method2.setText("");
        tv_method1.setVisibility(View.GONE);
        tv_method2.setVisibility(View.GONE);
      }
    });

    switch_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.getCheckedRadioButtonId() == btn_increase.getId()) {
          til.setHint("No. to increase");
        }
        if (radioGroup.getCheckedRadioButtonId() == btn_decrease.getId()) {
          til.setHint("No. to decrease");
        }
        calculateAndSetTVs();
      }
    });
  }

  private void calculateAndSetTVs() {
    int stitches;
    int changes;
    try {
      stitches = Integer.parseInt(Objects.requireNonNull(et_num_stitches.getText()).toString());
    } catch (Exception e) {
      Toast.makeText(getContext(), "Please input the number of stitches you are starting with", Toast.LENGTH_SHORT).show();
      return;
    }
    try {
      changes = Integer.parseInt(Objects.requireNonNull(et_num_change.getText()).toString());
    } catch (Exception e) {
      Toast.makeText(getContext(), "Please input the number of stitches to increase/decrease by", Toast.LENGTH_SHORT).show();
      return;
    }
    if (switch_mode.getCheckedRadioButtonId() == btn_increase.getId()) {
      Pair<String, String> increase = increase(stitches, changes);
      tv_method1.setText(String.format("Unbalanced:\n%s", increase.first));
      tv_method2.setText(String.format("More balanced:\n%s", increase.second));
    } else if (switch_mode.getCheckedRadioButtonId() == btn_decrease.getId()) {
      Pair<String, String> decrease = decrease(stitches, changes);
      tv_method1.setText(decrease.first);
      tv_method2.setText(decrease.second);
    }
    tv_method1.setVisibility(View.VISIBLE);
    tv_method2.setVisibility(View.VISIBLE);

  }

}
