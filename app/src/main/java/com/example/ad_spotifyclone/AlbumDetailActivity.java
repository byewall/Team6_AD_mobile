package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumDetailActivity extends AppCompatActivity {

    // creating variables on below line.
    String albumID = "";
    String albumImgUrl, albumName, artist, albumUrl;

    private TextView albumNameTV, artistTV;
    private ImageView albumIV;
    private FloatingActionButton playFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing variables on below line.
        setContentView(R.layout.activity_album_detail);
        albumID = getIntent().getStringExtra("id");
        albumIV = findViewById(R.id.idIVAlbum);
        albumImgUrl = getIntent().getStringExtra("img");
        albumName = getIntent().getStringExtra("name");
        artist = getIntent().getStringExtra("artist");
        albumUrl = getIntent().getStringExtra("albumUrl");
        Log.e("TAG", "album id is : " + albumID);
        albumNameTV = findViewById(R.id.idTVAlbumName);
        playFAB = findViewById(R.id.idFABPlay);
        artistTV = findViewById(R.id.idTVArtistName);
        // setting data on below line.
        albumNameTV.setText(albumName);
        artistTV.setText(artist);
        // adding click listener for fab on below line.
        playFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening album from url on below line.
                Uri uri = Uri.parse(albumUrl); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        // loading image on below line.
        Picasso.get().load(albumImgUrl).into(albumIV);
        // getting album tracks on below line.
        getAlbumTracks(albumID);
    }

    // method to get access token.
    private String getToken() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sh.getString("token", "Not Found");
    }

    // method to get tracks from albums.
    private void getAlbumTracks(String albumID) {
        // on below line creating variable for url
        String url = "https://api.spotify.com/v1/albums/" + albumID + "/tracks";
        // on below line creating list, initializing adapter and setting it to recycler view.
        ArrayList<TrackRVModal> trackRVModals = new ArrayList<>();
        TrackRVAdapter trackRVAdapter = new TrackRVAdapter(trackRVModals, this);
        RecyclerView trackRV = findViewById(R.id.idRVTracks);
        trackRV.setAdapter(trackRVAdapter);
        RequestQueue queue = Volley.newRequestQueue(AlbumDetailActivity.this);
        // on below line making json object request to parse json data.
        JsonObjectRequest trackObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemObj = itemsArray.getJSONObject(i);
                        String trackName = itemObj.getString("name");
                        String id = itemObj.getString("id");
                        String trackArtist = itemObj.getJSONArray("artists").getJSONObject(0).getString("name");
                        // on below line adding data to array list.
                        trackRVModals.add(new TrackRVModal(trackName, trackArtist, id));
                    }
                    trackRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AlbumDetailActivity.this, "Fail to get Tracks" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // on below line passing headers.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // on below line adding
        // request to queue.
        queue.add(trackObj);
    }
}