package com.jmulla.ukc;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

public class Settings extends PreferenceFragmentCompat {


  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    Context context = getPreferenceManager().getContext();
    PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

    ListPreference themePreference = new ListPreference(context);
    themePreference.setSummary("Choose theme settings");
    themePreference.setKey("theme_choice");

    //System default is for Android 10+ and battery default for lower versions
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
      themePreference.setEntries(new CharSequence[]{"Dark", "Light", "System default (recommended)"});
      themePreference.setEntryValues(new CharSequence[]{"0", "1", "2"});
    } else {
      themePreference.setEntries(new CharSequence[]{"Dark", "Light", "Set by Battery Saver (recommended)"});
      themePreference.setEntryValues(new CharSequence[]{"0", "1", "3"});
    }

    themePreference.setValueIndex(2);


    themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (getActivity() != null){
          getActivity().recreate();
        }
        return true;
      }
    });

    PreferenceCategory helpCategory = new PreferenceCategory(context);
    helpCategory.setKey("appearance");
    helpCategory.setTitle("Appearance");
    screen.addPreference(helpCategory);
    helpCategory.addPreference(themePreference);

    setPreferenceScreen(screen);
  }


}
