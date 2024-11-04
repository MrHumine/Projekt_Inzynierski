package com.example.inzynierskiprojekt;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntentLogin extends AppCompatActivity {

    Button buttonLogin;
    private FirebaseAuth  mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewLogin;
    private TextView textViewPassword;
    private TextView textViewWrong;
    String email;
    String password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_login);
        Intent intentMainMenu = new Intent(this, Menu.class);

        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        editTextEmail = (EditText) findViewById(R.id.login_email_edit_text);
        editTextPassword = (EditText) findViewById(R.id.login_password_edit_text);

        textViewLogin = findViewById(R.id.text_view_login);
        textViewPassword = findViewById(R.id.text_view_password);
        textViewWrong = findViewById(R.id.text_view_wrong);


        buttonLogin = (Button) findViewById(R.id.login_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);
        buttonLogin.setOnClickListener((view) -> {
//            startActivity(intentMainMenu);
//            finish();
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Uzupełnij brakujące dane", Toast.LENGTH_SHORT).show();
            }   else {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(IntentLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    textViewWrong.setText("Niepoprawny login lub hasło");
                                }
                            }
                        });

            }

            if(email.isEmpty()){
                textViewLogin.setText("Uzupełnij email");
            } else {
                textViewLogin.setText("");
            }
            if (password.isEmpty()){
                textViewPassword.setText("Uzupełnij brakujące hasło");
            } else {
                textViewPassword.setText("");
            }




        });



//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(IntentLogin.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });

    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intentMenu = new Intent(this, Menu.class);

            finish();
            startActivity(intentMenu);
            MainActivity.getInstance().finish();
        }
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
