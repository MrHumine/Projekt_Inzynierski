package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

public class FragmentPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        updateTheme(sharedPreferences);
        SettingsManager.applyTheme(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
            if (key.equals("dark_mode")) {
                updateTheme(sharedPreferences1);
            }
        });
        try {
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
}
