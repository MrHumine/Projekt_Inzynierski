package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class IntentRegister extends AppCompatActivity {
    Button buttonRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_register);
        buttonRegister = (Button) findViewById(R.id.register_button);
        Intent intentMenu = new Intent(this, Menu.class);
        buttonRegister = (Button) findViewById(R.id.register_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);
        buttonRegister.setOnClickListener((view) -> {
            startActivity(intentMenu);
        });

    }
}
