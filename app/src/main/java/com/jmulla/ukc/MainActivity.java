package com.jmulla.ukc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
  NavViewPager viewPager;


  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          viewPager.setCurrentItem(0);
          return true;
        case R.id.navigation_dashboard:
          viewPager.setCurrentItem(1);
          return true;
      }
      return false;
    }
  };


  public static void hideKeyboard(Context context, View view) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);

    //Removes the default title so we can use the custom one
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setDisplayShowTitleEnabled(false);
    }
    final BottomNavigationView navigation = findViewById(R.id.navigation);

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    viewPager = findViewById(R.id.nav_viewpager);
    final ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity.this.getSupportFragmentManager());
    adapter.addFragment(new IncDecFragement(), "inc_dec_fragment");
    adapter.addFragment(new ConversionFragment(), "conv_fragment");


    viewPager.addOnPageChangeListener(new OnPageChangeListener() {
      // This method will be invoked when the current page is scrolled
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
      }

      // Called when the scroll state changes:
      // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    viewPager.setOffscreenPageLimit(2);
    viewPager.setAdapter(adapter);
    viewPager.setCurrentItem(0);

    FloatingActionButton fab = findViewById(R.id.fab_feedback);

    fab.setOnClickListener(view -> {
      FeedbackDialog feedbackDialog = new FeedbackDialog();
      feedbackDialog.showDialog(this);

    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    //Changes color of taskbar
    Bitmap bm = BitmapFactory.decodeResource(getResources(), getApplicationInfo().icon);
    int color;

    String theme_choice = PreferenceManager.getDefaultSharedPreferences(this).getString("theme_choice", null);

    if (theme_choice == null) {
      //default is follow system
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    } else if (theme_choice.equals("0")) {
      //dark mode
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else if (theme_choice.equals("1")) {
      //light mode
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    } else if (theme_choice.equals("2")) {
      //system default
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    } else if (theme_choice.equals("3")) {
      //follow battery
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
    } else {
      //something went wrong
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }


    int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    switch (currentNightMode) {
      case Configuration.UI_MODE_NIGHT_YES:
        // Night mode is active, we're at night!
        color = getResources().getColor(R.color.grey900);
        break;
      // We don't know what mode we're in, assume notnight
      // Night mode is not active, we're in day time
      // by default we use light
      case Configuration.UI_MODE_NIGHT_NO:
      case Configuration.UI_MODE_NIGHT_UNDEFINED:
      default:
        color = getResources().getColor(R.color.colorPrimaryLight);
        break;
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      ActivityManager.TaskDescription taskDesc;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        taskDesc = new ActivityManager.TaskDescription(
            "Knitting Calculator", getApplicationInfo().icon, color);
      } else {
        taskDesc = new ActivityManager.TaskDescription(
            "Knitting Calculator", null, color);
      }
      this.setTaskDescription(taskDesc);
    }

    FloatingActionButton fab = findViewById(R.id.fab_feedback);
    //get the drawable
    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_baseline_feedback_24px);
    //copy it in a new one
    Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
    //set the color filter, you can use also Mode.SRC_ATOP
    willBeWhite.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    //set it to your fab button initialized before
    fab.setImageDrawable(willBeWhite);

  }

  /*
   * Listen for option item selections so that we receive a notification
   * when the user requests a refresh by selecting the refresh action bar item.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_settings) {
      Intent i = new Intent(this, SettingsActivity.class);
      startActivity(i);
    }

    // User didn't trigger a refresh, let the superclass handle this action
    return super.onOptionsItemSelected(item);

  }
}
