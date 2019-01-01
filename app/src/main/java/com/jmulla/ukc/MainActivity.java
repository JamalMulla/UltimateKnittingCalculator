package com.jmulla.ukc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
  NavViewPager viewPager;
  SharedPreferences sharedPref;

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
    final ViewPagerAdapter adapter = new ViewPagerAdapter (MainActivity.this.getSupportFragmentManager());
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
    switch (item.getItemId()) {
      case R.id.menu_dark_mode:
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
