package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class FragmentListOfFriends extends Fragment {
    RecyclerView recyclerView;
    ArrayList<FriendsData> list;
    private DatabaseReference dataBase;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycleViewInList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SettingsManager.applyTheme(sharedPreferences);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = currentUser.getUid();

        dataBase = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Friends")
                .child(userUid);

        list = new ArrayList<>();
        MyAdapter MyAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(MyAdapter);

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FriendsData friendsData = dataSnapshot.getValue(FriendsData.class);
                    friendsData.setId(dataSnapshot.getKey());
                    list.add(friendsData);
                }
                MyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public FragmentListOfFriends() {
        super(R.layout.fragment_list_of_friends);
    }

}
