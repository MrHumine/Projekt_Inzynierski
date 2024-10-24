package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class IntentLogin extends AppCompatActivity {
    Button buttonLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_login);
        Intent intentMainMenu = new Intent(this, Menu.class);

        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);

        buttonLogin = (Button) findViewById(R.id.login_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);
        buttonLogin.setOnClickListener((view) -> {
            startActivity(intentMainMenu);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.settings){
//            Toast.makeText(this, "Ustawienia" , Toast.LENGTH_SHORT).show();
            Intent intentUstawienia = new Intent(this, IntentUstawienia.class);
            startActivity(intentUstawienia);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.item_only_sett, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
