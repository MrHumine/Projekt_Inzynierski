package com.example.inzynierskiprojekt;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.ai.openai.models.ChatResponseMessage;
import com.azure.core.credential.AzureKeyCredential;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FragmentAddFriend extends Fragment {
    private static final String Tag = "Dodanie";
    TextInputEditText textInputEditTextName;
    ImageView imageViewName;
    TextInputEditText textInputEditTextLocalization;
    ImageView imageViewLocalization;
    TextInputEditText textInputEditTextHair;
    ImageView imageViewHair;
    TextInputEditText textInputEditTextEyes;
    ImageView imageViewEyes;
    TextInputEditText textInputEditTextDescription;
    ImageView imageViewDescription;
    TextInputEditText textInputEditTextBody;
    ImageView imageViewBody;
    TextInputEditText textInputEditTextSkin;
    ImageView imageViewSkin;
    TextInputEditText textInputEditTextHeight;
    ImageView imageViewHeight;
    TextView textViewError;
    Button buttonAdd;
    private DatabaseReference dataBase;
    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int RECOGNIZER_RESULT = 1;
    private String azureOpenaiKey = "9YcxWe84xST2aI8hYRjcifmi4PaqIQIuoQg7FX8N88MBek8OuSIqJQQJ99ALACHYHv6XJ3w3AAAAACOG8Nzp";
    private String endpoint = "https://ai-wrx758580291ai465395540385.services.ai.azure.com/";
    private String deploymentOrModelId = "gpt-4o-mini";
    private String tmpForSpeech = "";
    List<ChatRequestMessage> chatMessages = new ArrayList<>();
    OpenAIClient client = new OpenAIClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(azureOpenaiKey))
            .buildClient();
    private final ActivityResultLauncher<Intent> speechLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    ArrayList<String> recognizerText = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    assert recognizerText != null;
                    tmpForSpeech = recognizerText.get(0);
                    Log.d(Tag, tmpForSpeech);
                    textInputEditTextName.setText(tmpForSpeech);
                    chatMessages.clear();
                    chatMessages.add(new ChatRequestSystemMessage("Działasz w aplikacji mobilnej, w której jest możliwość zapisywania informacji o przyjaciołach. Przesłany zostanie tobie tekst, który użytkownik powiedział w celu opisania imienia. Twoim zadaniem jest zwrócić wyłącznie imienia lub pseudonimi, który ma opisywać tego przyjaciela. O to ten tekst: " + tmpForSpeech));
                    ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId, new ChatCompletionsOptions(chatMessages));
                    for (ChatChoice choice : chatCompletions.getChoices()) {
                        ChatResponseMessage message = choice.getMessage();
                        Log.d(Tag, message.getContent() + "1");
                        textInputEditTextName.setText(message.getContent());
                    }
                    if(recognizerText.isEmpty()){
                        Toast.makeText(getContext(), "Nie dualo sie rozpoznac mowy", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, "else?");
                    }
                }
            }
    );

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
        assert currentUser != null;
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

        imageViewName = view.findViewById(R.id.image_view_name);
        imageViewLocalization = view.findViewById(R.id.image_view_localization);
        imageViewBody = view.findViewById(R.id.image_view_body);
        imageViewEyes = view.findViewById(R.id.image_view_eyes);
        imageViewDescription = view.findViewById(R.id.image_view_description);
        imageViewHeight = view.findViewById(R.id.image_view_height);
        imageViewSkin = view.findViewById(R.id.image_view_skin);
        imageViewHair = view.findViewById(R.id.image_view_hair);

        textViewError = view.findViewById(R.id.textViewError);
        ViewGroup.LayoutParams params = textViewError.getLayoutParams();
        params.height = 0;
        textViewError.setLayoutParams(params);
        textViewError.setVisibility(View.INVISIBLE);

        dataBase = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Friends").child(userUid);

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_SPEECH_INPUT);
        }


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

                            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
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

        imageViewName.setOnClickListener(View -> {
            promptSpeechInput();

        });

        imageViewLocalization.setOnClickListener(View -> {

        });
        imageViewBody.setOnClickListener(View -> {

        });
        imageViewEyes.setOnClickListener(View -> {

        });
        imageViewDescription.setOnClickListener(View -> {

        });
        imageViewHeight.setOnClickListener(View -> {

        });
        imageViewSkin.setOnClickListener(View -> {

        });
        imageViewHair.setOnClickListener(View -> {

        });

    }

    private void promptSpeechInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("pl", "PL").toString());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Możesz mówić");
            speechLauncher.launch(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
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

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE_SPEECH_INPUT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                Toast.makeText(getContext(), "Nie udalo sie przyznać uprawnien", Toast.LENGTH_SHORT).show();
            }
        }
    }





}