package com.example.ad_spotifyclone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumRVAdapter extends RecyclerView.Adapter<AlbumRVAdapter.ViewHolder> {
    // creating variables for array list and context
    private ArrayList<AlbumRVModal> albumRVModalArrayList;
    private Context context;

    // creating a constructor.
    public AlbumRVAdapter(ArrayList<AlbumRVModal> albumRVModalArrayList, Context context) {
        this.albumRVModalArrayList = albumRVModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout file on below line.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRVAdapter.ViewHolder holder, int position) {
        // setting data to text view and image view on below line.
        AlbumRVModal albumRVModal = albumRVModalArrayList.get(position);
        Picasso.get().load(albumRVModal.imageUrl).into(holder.albumIV);
        holder.albumNameTV.setText(albumRVModal.name);
        holder.albumDetailTV.setText(albumRVModal.artistName);
        // adding click listener for album item on below line.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line opening a new album detail
                // activity for displaying songs within that album.
                Intent i = new Intent(context, AlbumDetailActivity.class);
                // on below line passing album related data.
                i.putExtra("id", albumRVModal.id);
                i.putExtra("name", albumRVModal.name);
                i.putExtra("img", albumRVModal.imageUrl);
                i.putExtra("artist", albumRVModal.artistName);
                i.putExtra("albumUrl", albumRVModal.external_urls);
                context.startActivity(i);
            }
        });
    }

    // on below line returning the size of list
    @Override
    public int getItemCount() {
        return albumRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // on below line creating variables
        // for image view and text view.
        private ImageView albumIV;
        private TextView albumNameTV, albumDetailTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // on below line initializing variables.
            albumIV = itemView.findViewById(R.id.idIVAlbum);
            albumNameTV = itemView.findViewById(R.id.idTVAlbumName);
            albumDetailTV = itemView.findViewById(R.id.idTVALbumDetails);
        }
    }
}