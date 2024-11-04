package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.example.inzynierskiprojekt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentAddFriend fragmentAddFriend;
    private FragmentListOfFriends fragmentListOfFriends;
    private FragmentSettings fragmentSettings;
    private FragmentPreferences fragmentPreferences;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
            Intent MainAcitvity = new Intent(this, MainActivity.class);
            startActivity(MainAcitvity);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentAddFriend = new FragmentAddFriend();
        fragmentListOfFriends = new FragmentListOfFriends();
        fragmentSettings = new FragmentSettings();
        fragmentPreferences = new FragmentPreferences();

        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);




        Button button1 = (Button) findViewById(R.id.buttonDodajPrzyjaciela);
        Button button2 = (Button) findViewById(R.id.buttonListaPrzyjaciol);
//        Button button3 = (Button) findViewById(R.id.buttonOpcje);

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMainMenu, fragmentAddFriend);
            fragmentTransaction.commit();
        } else {
            fragmentAddFriend = (FragmentAddFriend) fragmentManager.findFragmentByTag("add_friend");
            fragmentListOfFriends = (FragmentListOfFriends) fragmentManager.findFragmentByTag("list_of_freinds");
            fragmentSettings = (FragmentSettings) fragmentManager.findFragmentByTag("preferences");
        }

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

//        button3.setOnClickListener((view) ->{
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.replace(R.id.frameLayoutMainMenu, fragmentPreferences);
//            ft.commit();});

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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.settings){
//            Toast.makeText(this, "Ustawienia" , Toast.LENGTH_SHORT).show();
            Intent intentUstawienia = new Intent(this, IntentUstawienia.class);
            startActivity(intentUstawienia);
        } if (itemId == R.id.log_out){
            Intent mainActivity = new Intent(this, MainActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(mainActivity);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
