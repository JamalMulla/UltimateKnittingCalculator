package com.jmulla.ukc;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.jmulla.ukc.IncreasesDecreases.decrease;
import static com.jmulla.ukc.IncreasesDecreases.increase;

public class MainActivity extends AppCompatActivity {
    private RadioGroup switch_mode;
    private RadioButton btn_increase;
    private RadioButton btn_decrease;
    private TextInputEditText et_num_stitches;
    private TextInputEditText et_num_change;
    private TextInputLayout til;
    private TextView tv_method2;
    private TextView tv_method1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return false;
                case R.id.navigation_notifications:
                    return false;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Changes color of taskbar
        Bitmap bm = BitmapFactory.decodeResource(getResources(), getApplicationInfo().icon);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int color =  getResources().getColor(R.color.grey900);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Knitting Calculator", bm, color);
            this.setTaskDescription(taskDesc);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Removes the default title so we can use the custom one
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switch_mode = findViewById(R.id.switch_mode);
        btn_increase = findViewById(R.id.btn_increase);
        btn_decrease = findViewById(R.id.btn_decrease);
        Button btn_calc = findViewById(R.id.btn_calc);
        Button btn_clear = findViewById(R.id.btn_clear);
        et_num_stitches = findViewById(R.id.et_num_stitches);
        et_num_change = findViewById(R.id.et_num_change);
        til = findViewById(R.id.textInputLayout);
        tv_method1 = findViewById(R.id.tv_method1);
        tv_method2 = findViewById(R.id.tv_method2);



        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAndSetTVs();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_num_stitches.setText("");
                et_num_change.setText("");
                et_num_stitches.requestFocus();
                tv_method1.setText("");
                tv_method2.setText("");
                tv_method1.setVisibility(View.GONE);
                tv_method2.setVisibility(View.GONE);
            }
        });

        switch_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == btn_increase.getId()){
                    til.setHint("No. to increase");
                }
                if (radioGroup.getCheckedRadioButtonId() == btn_decrease.getId()){
                    til.setHint("No. to decrease");
                }
                calculateAndSetTVs();
            }
        });


    }

    private void calculateAndSetTVs() {
        boolean success = false;
        int stitches = 0;
        try {
            stitches = Integer.parseInt(et_num_stitches.getText().toString());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        int changes = 0;
        try {
            changes = Integer.parseInt(et_num_change.getText().toString());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success){
            if (switch_mode.getCheckedRadioButtonId() == btn_increase.getId()){
                Pair<String, String> increase = increase(stitches, changes);
                tv_method1.setText(increase.first);
                tv_method2.setText(increase.second);
            }else if (switch_mode.getCheckedRadioButtonId() == btn_decrease.getId()){
                Pair<String, String> decrease = decrease(stitches, changes);
                tv_method1.setText(decrease.first);
                tv_method2.setText(decrease.second);
            }
            tv_method1.setVisibility(View.VISIBLE);
            tv_method2.setVisibility(View.VISIBLE);
        }

    }

}
