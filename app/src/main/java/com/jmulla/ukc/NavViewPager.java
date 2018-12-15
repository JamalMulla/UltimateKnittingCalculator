package com.jmulla.ukc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class NavViewPager extends ViewPager {

  private boolean isPagingEnabled;

  public NavViewPager(Context context) {
    super(context);
    this.isPagingEnabled = true;
  }

  public NavViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.isPagingEnabled = true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onTouchEvent(event);
  }

  //for samsung phones to prevent tab switching keys to show on keyboard
  @Override
  public boolean executeKeyEvent(@NonNull KeyEvent event) {
    return isPagingEnabled && super.executeKeyEvent(event);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    return this.isPagingEnabled && super.onInterceptTouchEvent(event);
  }

  public void setPagingEnabled(boolean enabled) {
    this.isPagingEnabled = enabled;
  }
}