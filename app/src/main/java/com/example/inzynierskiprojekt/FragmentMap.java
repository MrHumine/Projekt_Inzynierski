package com.example.inzynierskiprojekt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentMap  extends Fragment implements OnMapReadyCallback {
    private static final String Tag = "Mapa";
    private DatabaseReference dataBase;
    private GoogleMap map;
    ArrayList<FriendsData> list;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
        String userUid = currentUser.getUid();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_in_fragment);
        if(supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        dataBase = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Friends")
                .child(userUid);
        list = new ArrayList<>();

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FriendsData friendsData = dataSnapshot.getValue(FriendsData.class);
                    friendsData.setId(dataSnapshot.getKey());
                    list.add(friendsData);
                    Log.d(Tag, list.get(0).name = "Przypisanie do listy");
                }
                updateMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }


        });


    }


    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                }
            }
        });
    }
    private void updateMap(){
        ArrayList<FriendsData> filteredList = new ArrayList<>();

        for (FriendsData friendsData : list) {
            Log.d(Tag, friendsData.name);
            if(!Objects.equals(friendsData.name, "") && !Objects.equals(friendsData.localization, "")){
                filteredList.add(friendsData);
            }
        }
        for (FriendsData friendsData : filteredList) {
            String[] fromLocalization = friendsData.localization.split(",");
            LatLng preciseLocalization = new LatLng(Double.parseDouble(fromLocalization[0]), Double.parseDouble(fromLocalization[1]));
            map.addMarker(new MarkerOptions().position(preciseLocalization).title(friendsData.name));
        }
        getLastLocation();
        if(currentLocation == null){
            Log.d(Tag, "Again null");
        } else {
            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(current).title("Tu jest lokalizacja"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f));

        }
        Log.d(Tag, "Working?");
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

        LatLng wroclaw = new LatLng(51.1093, 17.0386);
        googleMap.addMarker(new MarkerOptions().position(wroclaw).title("Tu jest lokalizacja"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wroclaw, 12.0f));
        getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            } else {
                Log.d(Tag, "Brak pozwolenia");
            }
        }
    }
}
