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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.example.inzynierskiprojekt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentAddFriend fragmentAddFriend;
    private FragmentListOfFriends fragmentListOfFriends;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

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

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        if(currentUser != null){
            currentUser.reload();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        fragmentAddFriend = new FragmentAddFriend();
        fragmentListOfFriends = new FragmentListOfFriends();

        Toolbar menuToolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMainMenu, fragmentAddFriend, "add_friends");
            fragmentTransaction.commit();
        } else {
            fragmentAddFriend = (FragmentAddFriend) fragmentManager.findFragmentByTag("add_friends");
            fragmentListOfFriends = (FragmentListOfFriends) fragmentManager.findFragmentByTag("list_of_friends");
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if(fragmentAddFriend == null){
                fragmentAddFriend = new FragmentAddFriend();
            }
                if(R.id.add_friend_navigation == item.getItemId()) {
                    if(fragmentAddFriend == null){
                        fragmentAddFriend = new FragmentAddFriend();
                    }
                    ft.replace(R.id.frameLayoutMainMenu, fragmentAddFriend, "add_friends");

                } else if(R.id.list_of_friends_navigation == item.getItemId()) {
                    if (fragmentListOfFriends == null){
                        fragmentListOfFriends = new FragmentListOfFriends();

                    }
                    ft.replace(R.id.frameLayoutMainMenu, fragmentListOfFriends, "list_of_friends");

                } else if(R.id.map_navigation == item.getItemId()) {
                    if(fragmentAddFriend == null){
                        fragmentAddFriend = new FragmentAddFriend();
                    }
                    ft.replace(R.id.frameLayoutMainMenu, fragmentListOfFriends, "list_of_friends");
                }
            ft.commit();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.settings){
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
