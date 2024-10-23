package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;

public class Menu extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentAddFriend fragmentAddFriend;
    private FragmentListOfFriends fragmentListOfFriends;
    private FragmentSettings fragmentSettings;
    private FragmentPreferences fragmentPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        fragmentManager = getSupportFragmentManager();
        fragmentAddFriend = new FragmentAddFriend();
        fragmentListOfFriends = new FragmentListOfFriends();
        fragmentSettings = new FragmentSettings();
        fragmentPreferences = new FragmentPreferences();

        Button button1 = (Button) findViewById(R.id.buttonDodajPrzyjaciela);
        Button button2 = (Button) findViewById(R.id.buttonListaPrzyjaciol);
        Button button3 = (Button) findViewById(R.id.buttonOpcje);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMainMenu, fragmentAddFriend);
        fragmentTransaction.commit();

        button1.setOnClickListener(View -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frameLayoutMainMenu, fragmentAddFriend);
            ft.commit();
        });

        button2.setOnClickListener(View -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frameLayoutMainMenu, fragmentListOfFriends);
            ft.commit();
        });

        button3.setOnClickListener((view) ->{
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frameLayoutMainMenu, fragmentPreferences);
            ft.commit();});

    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
//        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
//            if (key.equals("dark_mode")) {
//                updateTheme(sharedPreferences1);
//            } else if (key.equals("font_size")) {
//                updateFontSize(sharedPreferences1);
//
//            }
//        });
//    }
}
