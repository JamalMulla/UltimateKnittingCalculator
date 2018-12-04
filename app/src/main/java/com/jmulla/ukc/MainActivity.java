package com.jmulla.ukc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import static com.jmulla.ukc.IncreasesDecreases.decrease;
import static com.jmulla.ukc.IncreasesDecreases.increase;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Removes the default title so we can use the custom one
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Switch mode = findViewById(R.id.switch_mode);
        Button btn_calc = findViewById(R.id.btn_calc);
        Button btn_clear = findViewById(R.id.btn_clear);
        final EditText et_num_stitches = findViewById(R.id.et_num_stitches);
        final EditText et_num_change = findViewById(R.id.et_num_change);
        final TextView tv_method1 = findViewById(R.id.tv_method1);
        final TextView tv_method2 = findViewById(R.id.tv_method2);

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mode.isChecked()){
                    Pair<String, String> increase = increase(Integer.parseInt(et_num_stitches.getText().toString()), Integer.parseInt(et_num_change.getText().toString()));
                    tv_method1.setText(increase.first);
                    tv_method2.setText(increase.second);
                }else {
                    Pair<String, String> decrease = decrease(Integer.parseInt(et_num_stitches.getText().toString()), Integer.parseInt(et_num_change.getText().toString()));
                    tv_method1.setText(decrease.first);
                    tv_method2.setText(decrease.second);
                }
            }
        });
        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    compoundButton.setText("Decrease");
                } else {
                    compoundButton.setText("Increase");
                }
            }
        });

    }

}
