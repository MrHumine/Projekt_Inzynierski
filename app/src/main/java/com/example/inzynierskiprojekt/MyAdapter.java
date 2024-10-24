package com.example.inzynierskiprojekt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context conext;
    ArrayList<FriendsData> list;

    public MyAdapter(Context conext, ArrayList<FriendsData> arrayList) {
        this.conext = conext;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(conext).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        FriendsData friendsData = list.get(position);
        holder.name.setText(friendsData.getName());
        holder.localization.setText(friendsData.getLocalization());
        holder.character.setText(friendsData.getCharacter());
        holder.hair.setText(friendsData.getHair());
        holder.body.setText(friendsData.getBody());
        holder.eyes.setText(friendsData.getEyes());
        holder.height.setText(friendsData.getHeight());
        holder.skin.setText(friendsData.getSkin());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView localization;
        TextView character;
        TextView hair;
        TextView eyes;
        TextView skin;
        TextView height;
        TextView body;
        Button buttonDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            localization = itemView.findViewById(R.id.textViewLocalization);
            character = itemView.findViewById(R.id.textViewCharacter);
            hair = itemView.findViewById(R.id.textViewHair);
            eyes = itemView.findViewById(R.id.textViewEyes);
            skin = itemView.findViewById(R.id.textViewSkin);
            height = itemView.findViewById(R.id.textViewHeight);
            body = itemView.findViewById(R.id.textViewBody);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(view -> {
               removeItem(getAdapterPosition(), list.get(getAdapterPosition()).getId());
            });
        }
    }

    public void removeItem(int position, String id) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        DatabaseReference friendReference = FirebaseDatabase.getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("friends").child(id);
        friendReference.removeValue().addOnCompleteListener(task ->{
            if (task.isSuccessful()) {
                Log.d("Firebase", "Element usuniety z bazy");
            } else {
                Log.e("Firebase", "Blad podczas usuwania z bazy danych");
            }
        });
    }

}
