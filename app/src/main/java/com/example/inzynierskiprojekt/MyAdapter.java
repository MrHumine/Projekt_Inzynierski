package com.example.inzynierskiprojekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<FriendsData> list;

    public MyAdapter(Context context, ArrayList<FriendsData> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    public void setFilteredList(ArrayList<FriendsData> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        FriendsData friendsData = list.get(position);
        if(friendsData.getPhotoURL() != null){
            holder.imageView.setImageBitmap(toBitmap(friendsData.getPhotoURL()));
        }
        holder.name.setText(friendsData.getName());
        holder.localization.setText(friendsData.getLocalization());
        holder.character.setText(friendsData.getCharacter());
        holder.hair.setText(friendsData.getHair());
        holder.body.setText(friendsData.getBody());
        holder.eyes.setText(friendsData.getEyes());
        holder.height.setText(friendsData.getHeight());
        holder.skin.setText(friendsData.getSkin());
    }

    public Bitmap toBitmap(String photo){
        byte[] decoded = Base64.decode(photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView localization;
        TextView character;
        TextView hair;
        TextView eyes;
        TextView skin;
        TextView height;
        TextView body;
        Button buttonDelete;
        Button buttonEdit;
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
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            imageView = itemView.findViewById(R.id.photo_recycle);

            buttonDelete.setOnClickListener(view -> {
               removeItem(getAdapterPosition(), list.get(getAdapterPosition()).getId());
            });

            buttonEdit.setOnClickListener(view -> {

            });
        }
    }

    public void removeItem(int position, String id) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendReference = FirebaseDatabase
                .getInstance("https://inzynierskiprojekt-c436a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Friends")
                .child(userUid)
                .child(id);
        friendReference.removeValue().addOnCompleteListener(task ->{
            if (task.isSuccessful()) {
                Log.d("Firebase", "Element usuniety z bazy");
            } else {
                Log.e("Firebase", "Blad podczas usuwania z bazy danych");
            }
        });
    }
}
