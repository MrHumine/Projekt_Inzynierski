package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

public class IntentUstawienia extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentPreferences fragmentPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_settings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        fragmentManager = getSupportFragmentManager();
        fragmentPreferences = new FragmentPreferences();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_settings, fragmentPreferences);
        fragmentTransaction.commit();

    }
}
