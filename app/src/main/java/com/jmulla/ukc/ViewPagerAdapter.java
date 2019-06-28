package com.jmulla.ukc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {
  private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  ViewPagerAdapter(FragmentManager manager) {
    super(manager);
  }

  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList.size();
  }

  void addFragment(Fragment fragment, String title) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    final Fragment fragment = (Fragment) super.instantiateItem(container, position);
    instantiatedFragments.put(position, new WeakReference<>(fragment));
    return fragment;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    instantiatedFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  @Nullable
  Fragment getFragment(final int position) {
    final WeakReference<Fragment> wr = instantiatedFragments.get(position);
    if (wr != null) {
      return wr.get();
    } else {
      return null;
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }
}