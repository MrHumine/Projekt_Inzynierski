package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    Button buttonLogin;
    Button buttonRegister;
    static MainActivity mainActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainActivity = this;


        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        Intent intentLogin = new Intent(this, IntentLogin.class);
        Intent intentRegister = new Intent(this, IntentRegister.class);

        buttonLogin.setOnClickListener( (view) -> {
            startActivity(intentLogin);
        });

        buttonRegister.setOnClickListener( (view) -> {
            startActivity(intentRegister);
        });

    }

    static MainActivity getInstance(){
        return mainActivity;
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.settings){
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