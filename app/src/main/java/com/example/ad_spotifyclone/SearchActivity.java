package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    // on below line creating variables
    String searchQuery = "";
    private EditText searchEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // on below line initializing variables.
        searchEdt = findViewById(R.id.idEdtSearch);
        searchQuery = getIntent().getStringExtra("searchQuery");
        searchEdt.setText(searchQuery);
        // on below line adding action listener
        // for search edit text
        searchEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // on below line calling method to get tracks.
                    getTracks(searchEdt.getText().toString());
                    return true;
                }
                return false;
            }
        });
        // on below line getting tracks.
        getTracks(searchQuery);

    }

    // method to get token.
    private String getToken() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sh.getString("token", "Not Found");
    }


    private void getTracks(String searchQuery) {
        // on below line creating and initializing variables
        // for recycler view,list and adapter.
        RecyclerView songsRV = findViewById(R.id.idRVSongs);
        ArrayList<TrackRVModal> trackRVModals = new ArrayList<>();
        TrackRVAdapter trackRVAdapter = new TrackRVAdapter(trackRVModals, this);
        songsRV.setAdapter(trackRVAdapter);
        // on below line creating variable for url.
        String url = "https://api.spotify.com/v1/search?q=" + searchQuery + "&type=track";
        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        // on below line making json object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject trackObj = response.getJSONObject("tracks");
                    JSONArray itemsArray = trackObj.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemObj = itemsArray.getJSONObject(i);
                        String trackName = itemObj.getString("name");
                        String trackArtist = itemObj.getJSONArray("artists").getJSONObject(0).getString("name");
                        String trackID = itemObj.getString("id");
                        // on below line adding data to array list
                        trackRVModals.add(new TrackRVModal(trackName, trackArtist, trackID));
                    }
                    // on below line notifying adapter
                    trackRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Fail to get data : " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // on below line adding headers.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // adding json object request to queue.
        queue.add(jsonObjectRequest);

    }
}