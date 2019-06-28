package com.jmulla.ukc;

import android.app.Activity;
import android.app.ActivityManager;


import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
  NavViewPager viewPager;
  SharedPreferences sharedPref;
  private FirebaseAnalytics mFirebaseAnalytics;

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
    // Obtain the FirebaseAnalytics instance.
    mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    //Changes color of taskbar
    Bitmap bm = BitmapFactory.decodeResource(getResources(), getApplicationInfo().icon);
    int color;
    sharedPref = getSharedPreferences("com.jmulla.ka.prefs", Context.MODE_PRIVATE);

    if (sharedPref.getBoolean("ka_dark_mode", false)) {
      setTheme(R.style.FeedActivityThemeDark);
      color = getResources().getColor(R.color.grey900);
    } else {
      setTheme(R.style.FeedActivityThemeLight);
      color = getResources().getColor(R.color.colorPrimaryLight);
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      ActivityManager.TaskDescription taskDesc;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        taskDesc = new ActivityManager.TaskDescription(
            "Knitting Calculator", bm, color);
      } else {
        taskDesc = new ActivityManager.TaskDescription(
            "Knitting Calculator", null, color);
      }
      this.setTaskDescription(taskDesc);
    }

    getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
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
    //get the drawable
    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_baseline_feedback_24px);
    //copy it in a new one
    Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
    //set the color filter, you can use also Mode.SRC_ATOP

    willBeWhite.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    //set it to your fab button initialized before
    fab.setImageDrawable(willBeWhite);

    fab.setOnClickListener(view -> {
      FeedbackDialog feedbackDialog = new FeedbackDialog();
      feedbackDialog.showDialog(this);


//      //The optional file provider authority allows you to
//      //share the screenshot capture file to other apps (depending on your callback implementation)
//      new Maoni.Builder(null).withDefaultToEmailAddress("jamalm0101@gmail.com")
//          .build()
//          .start(MainActivity.this); //The screenshot captured is relative to this calling activity
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    MenuItem darkModeOption = menu.findItem(R.id.menu_dark_mode);
    //Set text and color for options

    if (sharedPref.getBoolean("ka_dark_mode", false)) {
      darkModeOption.setTitle("Disable Dark Mode");
    } else {
      darkModeOption.setTitle("Enable Dark Mode");
    }
    return true;
  }

  /*
   * Listen for option item selections so that we receive a notification
   * when the user requests a refresh by selecting the refresh action bar item.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_dark_mode) {
      if (sharedPref.getBoolean("ka_dark_mode", false)) {
        sharedPref.edit().putBoolean("ka_dark_mode", false).apply();
      } else {
        sharedPref.edit().putBoolean("ka_dark_mode", true).apply();
      }

      recreate();
      return true;
    }

    // User didn't trigger a refresh, let the superclass handle this action
    return super.onOptionsItemSelected(item);

  }
}
