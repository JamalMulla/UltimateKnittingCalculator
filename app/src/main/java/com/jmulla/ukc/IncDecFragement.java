package com.jmulla.ukc;

import static com.jmulla.ukc.IncreasesDecreases.decrease;
import static com.jmulla.ukc.IncreasesDecreases.increase;
import static com.jmulla.ukc.MainActivity.hideKeyboardFrom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
  private TextView tv_inc_dec_warning;
  private TextView tv_inc_dec_info;

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
    tv_inc_dec_warning = activity.findViewById(R.id.tv_inc_dec_warning);
    tv_inc_dec_info = activity.findViewById(R.id.tv_inc_dec_info);

    final AlertDialog textDialog = createTextDialog(getString(R.string.tv_inc_dec_info));
    final SpannableString spannable = SpannableString.valueOf("Confused? Click here");
    Utils.applySpan(spannable, "here", new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        textDialog.show();
      }
    });
    tv_inc_dec_info.setText(spannable);
    tv_inc_dec_info.setMovementMethod(LinkMovementMethod.getInstance());

    btn_calc.setOnClickListener(view -> {
      calculateAndSetTVs(true);
      hideKeyboardFrom(getContext(), tv_method1);
    });

    btn_clear.setOnClickListener(view -> {
      hideKeyboardFrom(getContext(), tv_method1);
      et_num_stitches.requestFocus();
      et_num_stitches.setText("");
      et_num_change.setText("");
      tv_method1.setText("");
      tv_method2.setText("");
      tv_method1.setVisibility(View.GONE);
      tv_method2.setVisibility(View.GONE);
      tv_inc_dec_warning.setVisibility(View.INVISIBLE);
      tv_inc_dec_info.setVisibility(View.VISIBLE);
    });

    switch_mode.setOnCheckedChangeListener((radioGroup, i) -> {
      if (radioGroup.getCheckedRadioButtonId() == btn_increase.getId()) {
        til.setHint("No. to increase");
      }
      if (radioGroup.getCheckedRadioButtonId() == btn_decrease.getId()) {
        til.setHint("No. to decrease");
      }
      calculateAndSetTVs(false);
    });
  }


  public AlertDialog createTextDialog(String text) {
    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
    final TextView input = new TextView(getContext());
    input.setText(text);
    input.setPadding(32, 32, 32, 16);
    input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    alert.setView(input);

    alert.setPositiveButton("Ok", (dialog, whichButton) -> {
    });

    return alert.create();
  }


  private void calculateAndSetTVs(boolean print_errors) {
    int stitches;
    int changes;
    tv_inc_dec_warning.setVisibility(View.INVISIBLE);

    try {
      stitches = Integer.parseInt(Objects.requireNonNull(et_num_stitches.getText()).toString());
      if (stitches <= 0) {
        if (print_errors) {
          Toast.makeText(getContext(), "Can't use 0 stitches", Toast.LENGTH_SHORT).show();
          return;
        }
      }
    } catch (Exception e) {
      if (print_errors) {
        Toast.makeText(getContext(), "Number of stitches not specified", Toast.LENGTH_SHORT).show();
      }
      return;
    }
    try {
      changes = Integer.parseInt(Objects.requireNonNull(et_num_change.getText()).toString());
      if (changes <= 0) {
        if (print_errors) {
          Toast.makeText(getContext(), "Can't use 0 stitches", Toast.LENGTH_SHORT).show();
          return;
        }
      }
    } catch (Exception e) {
      if (print_errors) {
        Toast.makeText(getContext(), "Number of stitches not specified", Toast.LENGTH_SHORT).show();
      }
      return;
    }
    if (print_errors) {
      tv_inc_dec_info.setVisibility(View.INVISIBLE);
    }
    if (switch_mode.getCheckedRadioButtonId() == btn_increase.getId()) {
      if (changes >= stitches) {

        String warning = getString(R.string.tv_inc_warning);
        tv_inc_dec_warning.setText(warning);
        tv_inc_dec_warning.setVisibility(View.VISIBLE);
      }
      Pair<String, String> increase = increase(stitches, changes);
      tv_method1.setText(String.format("Less balanced:\n%s", increase.first));
      tv_method2.setText(String.format("More balanced:\n%s", increase.second));
    } else if (switch_mode.getCheckedRadioButtonId() == btn_decrease.getId()) {
      if (changes > stitches / 2) {
        tv_inc_dec_warning.setText(getString(R.string.tv_dec_warning));
        tv_inc_dec_warning.setVisibility(View.VISIBLE);
      }
      Pair<String, String> decrease = decrease(stitches, changes);
      tv_method1.setText(String.format("Less balanced:\n%s", decrease.first));
      tv_method2.setText(String.format("More balanced:\n%s", decrease.second));
    }
    tv_method1.setVisibility(View.VISIBLE);
    tv_method2.setVisibility(View.VISIBLE);

  }

}
