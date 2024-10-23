package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class IntentLogin extends AppCompatActivity {
    Button buttonLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_login);
        Intent intentMainMenu = new Intent(this, Menu.class);
        buttonLogin = (Button) findViewById(R.id.login_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);
        buttonLogin.setOnClickListener((view) -> {
            startActivity(intentMainMenu);
        });
    }
}
