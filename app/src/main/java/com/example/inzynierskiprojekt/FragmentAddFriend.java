package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.Nullable;

public class FragmentAddFriend extends Fragment {

    TextInputEditText textInputEditTextName;
    TextInputEditText textInputEditTextLocalization;
    TextInputEditText textInputEditTextHair;
    TextInputEditText textInputEditTextEyes;
    TextInputEditText textInputEditTextDescription;
    TextInputEditText textInputEditTextBody;
    TextInputEditText textInputEditTextSkin;
    TextInputEditText textInputEditTextHeight;

//    TextView textViewName;
//    TextView textViewLocalization;
//    TextView textViewHairColour;
//    TextView textViewEyesColour;
//    TextView textViewDescription;
    Button buttonAdd;
    private DatabaseReference dataBase;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        buttonAdd = view.findViewById(R.id.button_add_description_creating_new_profile);

        textInputEditTextName = view.findViewById(R.id.text_input_add_name);
        textInputEditTextHair = view.findViewById(R.id.text_input_add_hair);
        textInputEditTextEyes = view.findViewById(R.id.text_input_add_eyes);
        textInputEditTextDescription = view.findViewById(R.id.text_input_add_description);
        textInputEditTextLocalization = view.findViewById(R.id.text_input_add_localization);
        textInputEditTextBody = view.findViewById(R.id.text_input_body);
        textInputEditTextSkin = view.findViewById(R.id.text_input_skin);
        textInputEditTextHeight = view.findViewById(R.id.text_input_height);



        dataBase = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("friends");

        buttonAdd.setOnClickListener(View -> {
            String name = String.valueOf(textInputEditTextName.getText());
            String localization = String.valueOf(textInputEditTextLocalization.getText());
            String hair = String.valueOf(textInputEditTextHair.getText());
            String eyes = String.valueOf(textInputEditTextEyes.getText());

            String character = String.valueOf(textInputEditTextDescription.getText());
            String body = String.valueOf(textInputEditTextBody.getText());
            String skin = String.valueOf(textInputEditTextSkin.getText());
            String height = String.valueOf(textInputEditTextHeight.getText());

            String userId = dataBase.push().getKey();

            FriendsData friendData = new FriendsData(userId, name, hair, eyes, character, localization, skin, height, body);
            if (userId != null) {
                dataBase.child(userId).setValue(friendData)
                        .addOnSuccessListener(aVoid -> {
                            textInputEditTextName.setText("");
                            textInputEditTextEyes.setText("");
                            textInputEditTextDescription.setText("");
                            textInputEditTextHair.setText("");
                            textInputEditTextLocalization.setText("");
                            textInputEditTextBody.setText("");
                            textInputEditTextSkin.setText("");
                            textInputEditTextHeight.setText("");

                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }



    @Override
    public void onResume(){
        super.onResume();
    }

    public FragmentAddFriend() {

        super(R.layout.fragment_add_friend);

    }
}