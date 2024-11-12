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

import org.w3c.dom.Text;

public class IntentRegister extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button buttonRegister;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextRePassword;
    TextView textViewUnderButton;
    TextView textViewEmail;
    TextView textViewPassword;
    TextView textViewRePassword;
    String email;
    String password;
    String passwordRe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_register);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsManager.applyTheme(sharedPreferences);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        buttonRegister = (Button) findViewById(R.id.register_button);
        buttonRegister = (Button) findViewById(R.id.register_button);

        editTextEmail = findViewById(R.id.register_email_edit_text);
        editTextPassword = findViewById(R.id.register_password_edit_text);
        editTextRePassword = findViewById(R.id.register_re_password_edit_text);

        textViewUnderButton = findViewById(R.id.text_view_register_under_button);
        textViewEmail = findViewById(R.id.text_view_register_email);
        textViewPassword = findViewById(R.id.text_view_register_password);
        textViewRePassword = findViewById(R.id.text_view_register_re_password);

        Intent intentMenu = new Intent(this, Menu.class);

        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(menuToolbar);


        buttonRegister.setOnClickListener((view) -> {

            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            passwordRe = editTextRePassword.getText().toString();

            if (!password.equals(passwordRe)){
                textViewPassword.setText("Hsała się nie zgadzają");
                textViewRePassword.setText("Hasła się nie zgadzają");
            } else {
                textViewPassword.setText("");
                textViewRePassword.setText("");
            }

            if(!email.isEmpty() && !password.isEmpty() && password.equals(passwordRe)){

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(IntentRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                        });
            } else {
                textViewUnderButton.setText("Niepoprawne dane");
            }

        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
            finish();
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
