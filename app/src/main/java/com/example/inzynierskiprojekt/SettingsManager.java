package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class SettingsManager {
    private SharedPreferences sharedPreferences;
    public SettingsManager(SharedPreferences sharedPreferences){
    this.sharedPreferences = sharedPreferences;
    }
    public static void applyTheme(SharedPreferences sharedPreferences){
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}





















