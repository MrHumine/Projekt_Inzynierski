package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.Nullable;

public class FragmentMap  extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
        String userUid = currentUser.getUid();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public FragmentMap() {
        super(R.layout.fragment_mapa);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
            Intent MainAcitvity = new Intent(getContext(), MainActivity.class);
            startActivity(MainAcitvity);
            getActivity().finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
    }
}
