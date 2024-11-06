package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    TextView textViewError;
    Button buttonAdd;
    private DatabaseReference dataBase;
    private FirebaseAuth mAuth;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
        String userUid = currentUser.getUid();

        buttonAdd = view.findViewById(R.id.button_add_description_creating_new_profile);

        textInputEditTextName = view.findViewById(R.id.text_input_add_name);
        textInputEditTextHair = view.findViewById(R.id.text_input_add_hair);
        textInputEditTextEyes = view.findViewById(R.id.text_input_add_eyes);
        textInputEditTextDescription = view.findViewById(R.id.text_input_add_description);
        textInputEditTextLocalization = view.findViewById(R.id.text_input_add_localization);
        textInputEditTextBody = view.findViewById(R.id.text_input_body);
        textInputEditTextSkin = view.findViewById(R.id.text_input_skin);
        textInputEditTextHeight = view.findViewById(R.id.text_input_height);

        textViewError = view.findViewById(R.id.textViewError);
        ViewGroup.LayoutParams params = textViewError.getLayoutParams();
        params.height = 0;
        textViewError.setLayoutParams(params);
        textViewError.setVisibility(View.INVISIBLE);

        dataBase = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Friends").child(userUid);

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

            FriendsData friendData = new FriendsData(userId, name, localization, character, hair, eyes, skin, height, body);
            if (!name.isEmpty() || !localization.isEmpty() || !hair.isEmpty() || !eyes.isEmpty() ||
                    !character.isEmpty() || !body.isEmpty() || !skin.isEmpty() || !height.isEmpty()) {
                if (userId != null) {
                    dataBase.child(userId).setValue(friendData)
                            .addOnSuccessListener(aVoid -> {
                                textInputEditTextName.setText("");
                                textInputEditTextLocalization.setText("");
                                textInputEditTextDescription.setText("");
                                textInputEditTextHair.setText("");
                                textInputEditTextEyes.setText("");
                                textInputEditTextSkin.setText("");
                                textInputEditTextHeight.setText("");
                                textInputEditTextBody.setText("");

                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            });
                    textViewError.setText("");
                    params.height = 0;
                    textViewError.setLayoutParams(params);
                }
            } else {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText("Uzupełnij przynjamniej jedno pole");
                params.height = 50;
                textViewError.setLayoutParams(params);
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

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
            Intent MainAcitvity = new Intent(getContext(), MainActivity.class);
            startActivity(MainAcitvity);
            getActivity().finish();
        }
    }

}