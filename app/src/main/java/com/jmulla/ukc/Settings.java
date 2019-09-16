package com.jmulla.ukc;


import android.content.Context;
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
    themePreference.setEntries(new CharSequence[]{"Dark", "Light", "System default (recommended)"});
    themePreference.setEntryValues(new CharSequence[]{"0", "1", "2"});
    themePreference.setValue("2");
    themePreference.setSummary("Choose theme settings");
    themePreference.setKey("theme_choice");

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
