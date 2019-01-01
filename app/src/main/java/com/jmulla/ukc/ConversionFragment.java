package com.jmulla.ukc;

import static com.jmulla.ukc.Conversions.calculateAmounts;
import static com.jmulla.ukc.MainActivity.hideKeyboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConversionFragment extends Fragment {

  private EditText et_yardage;
  private EditText et_skein_weight;
  private EditText et_skein_yardage;
  private TextView tv_yarn_weight;
  private Spinner spinner_yardage;
  private Spinner spinner_weight;
  private Spinner spinner_skein_yardage;
  private TextView tv_conv_info;
  private TextView tv_num_balls;
  Button btn_convert;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_convert, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    FragmentActivity activity = getActivity();
    et_yardage = activity.findViewById(R.id.et_yardage);
    et_skein_weight = activity.findViewById(R.id.et_skein_weight);
    et_skein_yardage = activity.findViewById(R.id.et_skein_yardage);
    tv_yarn_weight = activity.findViewById(R.id.tv_yarn_weight);
    tv_num_balls = activity.findViewById(R.id.tv_num_balls);
    btn_convert = activity.findViewById(R.id.btn_convert);
    Button btn_clear = activity.findViewById(R.id.btn_convert_clear);
    spinner_yardage = activity.findViewById(R.id.spinner_yardage);
    spinner_weight = activity.findViewById(R.id.spinner_weight);
    spinner_skein_yardage = activity.findViewById(R.id.spinner_skein_yardage);
    tv_conv_info = activity.findViewById(R.id.tv_conv_info);
    tv_conv_info.setText(getString(R.string.tv_conv_info));
    tv_conv_info.setMovementMethod(LinkMovementMethod.getInstance());

    final AlertDialog textDialog = createTextDialog(getString(R.string.tv_conv_info));
    final SpannableString spannable = SpannableString.valueOf("Confused? Click here");
    Utils.applySpan(spannable, "here", new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        textDialog.show();
      }
    });
    tv_conv_info.setText(spannable);
    tv_conv_info.setMovementMethod(LinkMovementMethod.getInstance());

    // Adapter for yardage
    ArrayAdapter<String> yardage_adapter = new ArrayAdapter<>(activity,
        R.layout.custom_spinner_item);
    yardage_adapter.add("Yards");
    yardage_adapter.add("Metres");

    ArrayAdapter<String> weight_adapter = new ArrayAdapter<>(activity,
        R.layout.custom_spinner_item);
    weight_adapter.add("Grams");
    weight_adapter.add("Ounces");

    yardage_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
    weight_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
    spinner_yardage.setAdapter(yardage_adapter);
    spinner_skein_yardage.setAdapter(yardage_adapter);
    spinner_weight.setAdapter(weight_adapter);

    btn_convert.setOnClickListener(view -> {
      calculateAndSetTvs();
      hideKeyboard(getContext(), tv_yarn_weight);

    });

    btn_clear.setOnClickListener(view -> {
      hideKeyboard(getContext(), tv_yarn_weight);
      et_yardage.requestFocus();
      et_yardage.setText("");
      et_skein_weight.setText("");
      et_skein_yardage.setText("");
      tv_yarn_weight.setText("");
      tv_num_balls.setText("");
      tv_yarn_weight.setVisibility(View.GONE);
      tv_num_balls.setVisibility(View.GONE);
      tv_conv_info.setVisibility(View.VISIBLE);
    });
  }

  private void calculateAndSetTvs() {
    double patternYarn;
    double ballWeight;
    double ballYarn;
    try {
      patternYarn = Double.valueOf(et_yardage.getText().toString());
    } catch (NumberFormatException e) {
      Toast.makeText(getContext(), "Yarn amount for project not specified", Toast.LENGTH_SHORT)
          .show();
      return;
    }
    try {
      ballWeight = Double.valueOf(et_skein_weight.getText().toString());
    } catch (NumberFormatException e) {
      Toast.makeText(getContext(), "Weight of skein not specified", Toast.LENGTH_SHORT).show();
      return;
    }
    try {
      ballYarn = Double.valueOf(et_skein_yardage.getText().toString());
    } catch (NumberFormatException e) {
      Toast.makeText(getContext(), "Yardage of skein not specified", Toast.LENGTH_SHORT).show();
      return;
    }
    tv_conv_info.setVisibility(View.INVISIBLE);
    double patternYarnMetres = patternYarn;
    double ballWeightGrams = ballWeight;
    double ballYarnMetres = ballYarn;
    if (spinner_yardage.getSelectedItem().toString().equals("Yards")) {
      patternYarnMetres = Utils.yardsToMetres(patternYarn);
    }
    if (spinner_weight.getSelectedItem().toString().equals("Ounces")) {
      ballWeightGrams = Utils.ouncesToGrams(ballWeight);
    }
    if (spinner_skein_yardage.getSelectedItem().toString().equals("Yards")) {
      ballYarnMetres = Utils.yardsToMetres(ballYarn);
    }

    Pair<Double, Double> doubleDoublePair = calculateAmounts(patternYarnMetres, ballWeightGrams,
        ballYarnMetres);
    if (doubleDoublePair == null) {
      System.out.println("Invalid values\n");
    } else {
      double yarn_weight = doubleDoublePair.first;
      if (spinner_weight.getSelectedItem().toString().equals("Ounces")) {
        yarn_weight = Utils.roundToDP(Utils.gramsToOunces(yarn_weight), 2);
        tv_yarn_weight.setText(String.format("You will need %s ounces of yarn.", yarn_weight));
      } else {
        tv_yarn_weight.setText(
            String.format("You will need %s grams of yarn.", Utils.roundToDP(yarn_weight, 2)));
      }

      String num_balls = String.valueOf(Utils.roundToDP(doubleDoublePair.second, 2));
      tv_num_balls.setText(String
          .format("This is %s skeins/balls. Best to round up when buying", num_balls));
      tv_yarn_weight.setVisibility(View.VISIBLE);
      tv_num_balls.setVisibility(View.VISIBLE);
    }
  }

  public AlertDialog createTextDialog(String text) {
    Context context = getContext();
    Builder alert = new Builder(context);
    final TextView input = new TextView(context);
    input.setLinksClickable(true);
    input.setClickable(true);
    input.setMovementMethod(LinkMovementMethod.getInstance());
    input.setPadding(32, 32, 32, 16);
    input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    input.setText(Html.fromHtml(text));
    alert.setView(input);
    alert.setPositiveButton("Ok", (dialog, whichButton) -> {
    });

    return alert.create();
  }
}
