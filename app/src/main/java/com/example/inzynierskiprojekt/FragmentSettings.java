package com.example.inzynierskiprojekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

public class FragmentSettings extends Fragment {
    Boolean isNighModeOn;
    Button buttonLogOut;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            isNighModeOn = false;
        } else {
            isNighModeOn = true;
        }
        buttonLogOut = view.findViewById(R.id.button_log_out);

        buttonLogOut.setOnClickListener(View -> {
            if(isNighModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isNighModeOn = false;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isNighModeOn = true;
            }
        });
    }
    public FragmentSettings() {
        super(R.layout.fragment_settings);
    }
}
