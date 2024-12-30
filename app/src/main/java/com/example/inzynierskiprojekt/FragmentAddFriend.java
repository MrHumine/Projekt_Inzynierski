package com.example.inzynierskiprojekt;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class FragmentAddFriend extends Fragment {
    private static final String Tag = "Dodanie";
    ImageView imageViewPhoto;
    ImageView imageViewWholeDescription;
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
    Button buttonAddPhoto;
    private DatabaseReference dataBase;
    private FirebaseAuth mAuth;
    int SELECT_PICTURE = 200;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int RECOGNIZER_RESULT = 1;
    private String azureOpenaiKey = "9YcxWe84xST2aI8hYRjcifmi4PaqIQIuoQg7FX8N88MBek8OuSIqJQQJ99ALACHYHv6XJ3w3AAAAACOG8Nzp";
    private String endpoint = "https://ai-wrx758580291ai465395540385.services.ai.azure.com/";
    private String deploymentOrModelId = "gpt-4o-mini";
    private String tmpForSpeech = "";
    private String instructionToAi = "";
    private int number = 0;
    private Uri selectedImageUri;
    List<ChatRequestMessage> chatMessages = new ArrayList<>();
    OpenAIClient client = new OpenAIClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(azureOpenaiKey))
            .buildClient();
    String photoURL;
    Bitmap selectededImageBitmap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
        assert currentUser != null;
        String userUid = currentUser.getUid();

        buttonAdd = view.findViewById(R.id.button_add_description_creating_new_profile);
        buttonAddPhoto = view.findViewById(R.id.button_add_image);

        textInputEditTextName = view.findViewById(R.id.text_input_add_name);
        textInputEditTextHair = view.findViewById(R.id.text_input_add_hair);
        textInputEditTextEyes = view.findViewById(R.id.text_input_add_eyes);
        textInputEditTextDescription = view.findViewById(R.id.text_input_add_description);
        textInputEditTextLocalization = view.findViewById(R.id.text_input_add_localization);
        textInputEditTextBody = view.findViewById(R.id.text_input_body);
        textInputEditTextSkin = view.findViewById(R.id.text_input_skin);
        textInputEditTextHeight = view.findViewById(R.id.text_input_height);

        imageViewPhoto = view.findViewById(R.id.image_view_add_photo);
        imageViewWholeDescription = view.findViewById(R.id.image_view_whole_description);
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

        buttonAddPhoto.setOnClickListener(View -> {
            imageChoose();
        });

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
            photoURL = "";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectededImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            photoURL = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d(Tag, photoURL);

            FriendsData friendData = new FriendsData(userId, name, localization, character, hair, eyes, skin, height, body, photoURL);
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

        imageViewWholeDescription.setOnClickListener(View -> {
        instructionToAi = "Użytkownik wypowiadając ten tekst opisywał wszystkie lub część róbryk" +
                " dostępnych w aplikacji. Są to po kolei: Imie/Pseudonim, lokalizacja gdzie " +
                "spotkało się osobę, charakter" +
                ",kolor włosów, kolor oczu, odcień skóry, wrost i sylwetka. Twoim zadaniem " +
                "jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji " +
                "dotyczących rubryk wymienionych w poprzednim zdaniu. Zwróć odpowiedź w formie " +
                "nazwy rubryki dwukropek wyciągnięte informacje.(Jeżeli żadnych informacji nie" +
                " będzie to zwróć pusty string) ";
        number = 9;
        promptSpeechInput();
        });

        imageViewName.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. " +
                    "Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i " +
                    "zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 1;
            promptSpeechInput();
        });

        imageViewLocalization.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał lokalizację, w której " +
                    "poznał daną osobę. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym " +
                    "tekście i zostawienie informacji dotyczących tylko lokalizacji. ";
            number = 2;
            promptSpeechInput();
        });

        imageViewBody.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 3;
            promptSpeechInput();
        });
        imageViewEyes.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 4;
            promptSpeechInput();
        });
        imageViewDescription.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 5;
            promptSpeechInput();
        });
        imageViewHeight.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 6;
            promptSpeechInput();
        });
        imageViewSkin.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 7;
            promptSpeechInput();
        });
        imageViewHair.setOnClickListener(View -> {
            instructionToAi = "Użytkownik wypowiadając ten tekst opisywał imię albo pseudonim. Twoim zadaniem jest usunięcie niepotrzebnych słów w tym tekście i zostawienie informacji dotyczących tylko imienia albo pseudonimu. ";
            number = 8;
            promptSpeechInput();
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                new Locale("pl", "PL").toString());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        String tmpSpeech = "";
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                tmpSpeech =  Objects.requireNonNull(result).get(0);
                try {
                    tmpSpeech = questionToAi(tmpSpeech);
                } catch (ActivityNotFoundException e) {
                }
                tmpForSpeech = tmpSpeech;
            }
        }
    }

    private String questionToAi(String anwser){
        chatMessages.clear();
        try {
            chatMessages.add(new ChatRequestSystemMessage("Działasz w aplikacji " +
                    "mobilnej, która pozwala na zapisyanie informacji o nowo poznanych ludziach. " +
                    "Dostaniesz tekst, który został wypowiedziany przez użytkownika i zamieniony " +
                    "na tekst. " + instructionToAi + "O to ten tekst: " + anwser));

            ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId,
                    new ChatCompletionsOptions(chatMessages));

            for (ChatChoice choice : chatCompletions.getChoices()) {
                ChatResponseMessage message = choice.getMessage();
                Log.d(Tag, message.getContent() + number);
                anwser = message.getContent();
                tmpForSpeech = anwser;
            }

            switch(number){
                case 1:
                    textInputEditTextName.setText(anwser);
                    break;
                case 2:
                    textInputEditTextLocalization.setText(anwser);
                    break;
                case 3:
                    textInputEditTextDescription.setText(anwser);
                    break;
                case 4:
                    textInputEditTextHair.setText(anwser);
                    break;
                case 5:
                    textInputEditTextEyes.setText(anwser);
                    break;
                case 6:
                    textInputEditTextSkin.setText(anwser);
                    break;
                case 7:
                    textInputEditTextHeight.setText(anwser);
                    break;
                case 8:
                    textInputEditTextBody.setText(anwser);
                    break;
                case 9:
                    String[] divideAnswer = anwser.split("\n");

                    textInputEditTextName.setText(divideAnswer[0].split(":")[1]);
                    textInputEditTextLocalization.setText(divideAnswer[1].split(":")[1]);
                    textInputEditTextDescription.setText(divideAnswer[2].split(":")[1]);
                    textInputEditTextHair.setText(divideAnswer[3].split(":")[1]);
                    textInputEditTextEyes.setText(divideAnswer[4].split(":")[1]);
                    textInputEditTextSkin.setText(divideAnswer[5].split(":")[1]);
                    textInputEditTextHeight.setText(divideAnswer[6].split(":")[1]);
                    textInputEditTextBody.setText(divideAnswer[7].split(":")[1]);
                    break;
            }

            return anwser;
        } catch (ActivityNotFoundException e) {
        Log.d(Tag, anwser + " quaetionAi bład");
    }
        return anwser;
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

    private void imageChoose(){
        Intent intentImageChoose = new Intent();
        intentImageChoose.setType("image/*");
        intentImageChoose.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(intentImageChoose);
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            if(data != null && data.getData() != null){
                selectedImageUri = data.getData();
                try {
                    selectededImageBitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().getContentResolver(), selectedImageUri);
                    imageViewPhoto.setImageBitmap(selectededImageBitmap);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    });

}