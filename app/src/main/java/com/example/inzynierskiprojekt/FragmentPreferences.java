package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class FragmentPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        updateTheme(sharedPreferences);
//        updateFontSize(sharedPreferences);
        SettingsManager.applyTheme(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
            if (key.equals("dark_mode")) {
                updateTheme(sharedPreferences1);
            }
//            else if (key.equals("font_size")) {
//                updateFontSize(sharedPreferences1);
//
//            }
        });
        try {
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SettingsManager.applyTheme(sharedPreferences);
        } catch (Exception e) {

    }
}

private void updateTheme(SharedPreferences sharedPreferences){
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
}

//    private void updateFontSize(SharedPreferences sharedPreferences){
//        int fontSize = sharedPreferences.getInt("font_size", 16);
//    }
}
