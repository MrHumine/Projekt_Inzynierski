package com.example.inzynierskiprojekt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
    private SearchView searchView;
    MyAdapter myAdapter;

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
        myAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        searchView = view.findViewById(R.id.searchViewInList);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FriendsData friendsData = dataSnapshot.getValue(FriendsData.class);
                    friendsData.setId(dataSnapshot.getKey());
                    list.add(friendsData);
                }
                myAdapter.notifyDataSetChanged();
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

    private void filterList(String newText){
        ArrayList<FriendsData> filteredList = new ArrayList<>();
        for (FriendsData friendsData : list){

            if (friendsData.body.equalsIgnoreCase(newText) ||
                    friendsData.character.equalsIgnoreCase(newText) ||
                    friendsData.eyes.equalsIgnoreCase(newText) ||
                    friendsData.hair.equalsIgnoreCase(newText) ||
                    friendsData.localization.equalsIgnoreCase(newText) ||
                    friendsData.name.equalsIgnoreCase(newText) ||
                    friendsData.skin.equalsIgnoreCase(newText)){
                filteredList.add(friendsData);
            }
        }
        if (filteredList.isEmpty()){
//            Toast.makeText(getContext(), "Brak wyników, musisz być bardziej precyzyjny", Toast.LENGTH_SHORT).show();
        } else {
            myAdapter.setFilteredList(filteredList);
        }
    }
}
