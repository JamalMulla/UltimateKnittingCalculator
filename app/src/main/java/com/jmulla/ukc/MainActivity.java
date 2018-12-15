package com.jmulla.ukc;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

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
      int color = ContextCompat.getColor(this, R.color.grey900);
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
    viewPager.setAdapter(adapter);
    viewPager.setCurrentItem(0);

  }
}
