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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntentLogin extends AppCompatActivity {

    private Button buttonLogin;
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

        editTextEmail = findViewById(R.id.login_email_edit_text);
        editTextPassword = findViewById(R.id.login_password_edit_text);
        textViewLogin = findViewById(R.id.text_view_login);
        textViewPassword = findViewById(R.id.text_view_password);
        textViewWrong = findViewById(R.id.text_view_wrong);

        buttonLogin = findViewById(R.id.login_button);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        buttonLogin.setOnClickListener((view) -> {
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            if(isEmailOrPasswordEmpty(email, password)){
                Toast.makeText(this, "Uzupełnij brakujące dane", Toast.LENGTH_SHORT).show();
            } else {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(IntentLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                textViewWrong.setText("Niepoprawny login lub hasło");
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

    }

    public static Boolean isEmailOrPasswordEmpty(String email, String password){
        return email.isEmpty() || password.isEmpty();
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent menu = new Intent(this, Menu.class);

            finish();
            startActivity(menu);
//            MainActivity.getInstance().finish();
        }
    }

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
