package com.example.ad_spotifyclone;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // on below line calling method to
        // load our recycler views and search view.
        initializeAlbumsRV();
        initializePopularAlbumsRV();
        initializeTrendingAlbumsRV();
        initializeSearchView();

        ImageButton recordBtn = findViewById(R.id.idRecording);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission is not granted
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                } else {
                    // permission has already been granted
                    startRecordActivity();
                }
            }
        });
    }

    private void startRecordActivity() {
        Intent intent = new Intent(MainActivity.this, RecordActivity.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecordActivity();
            } else {
                Toast.makeText(this, "Recording audio permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // below method is use to initialize search view.
    private void initializeSearchView() {
        // on below line initializing our
        // edit text for search views.
        EditText searchEdt = findViewById(R.id.idEdtSearch);
        searchEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // on below line calling method to search tracks.
                    searchTracks(searchEdt.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    // below method is use to open search
    // tracks activity to search tracks.
    private void searchTracks(String searchQuery) {
        // on below line opening search activity to
        // display search results in search activity.
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        i.putExtra("searchQuery", searchQuery);
        startActivity(i);
    }

    // below method is use to get token.
    private String getToken() {
        // on below line getting token from shared prefs.
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sh.getString("token", "Not Found");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // on below line calling generate
        // token method to generate token.
        generateToken();
    }

    private void generateToken() {
        // on below line creating a variable for
        // url to generate access token
        String url = "https://accounts.spotify.com/api/token?grant_type=client_credentials";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // on below line making string request to generate access token.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line getting access token and
                    // saving it to shared preferences.
                    String tk = jsonObject.getString("access_token");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("token", "Bearer " + tk);
                    myEdit.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                // on below line passing headers.
                // Make sure to add your authorization.
                headers.put("Authorization", "Basic ZjBlMDViYjZiZmUzNDNhYzkyMTEwZDA3OWRhYWY0Yjk6OWJmYTM2NjQwNTBmNDhlYTk2ZGE2YzA4MzcwNGU2ZDQ=");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        // adding request to queue.
        queue.add(request);
    }


    private void initializeAlbumsRV() {
        // on below line initializing albums rv
        RecyclerView albumsRV = findViewById(R.id.idRVAlbums);
        // on below line creating list, initializing adapter
        // and setting it to recycler view.
        ArrayList<AlbumRVModal> albumRVModalArrayList = new ArrayList<>();
        AlbumRVAdapter albumRVAdapter = new AlbumRVAdapter(albumRVModalArrayList, this);
        albumsRV.setAdapter(albumRVAdapter);
        // on below line creating a variable for url
        String url = "https://api.spotify.com/v1/albums?ids=07w0rG5TETcyihsEIZR3qG%2C5iVEe1GHMtvUHwwqArThFa%2C3lS1y25WAhcqJDATJK70Mq";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // on below line making json object request to parse json data.
        JsonObjectRequest albumObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray albumArray = response.getJSONArray("albums");
                    for (int i = 0; i < albumArray.length(); i++) {
                        JSONObject albumObj = albumArray.getJSONObject(i);
                        String album_type = albumObj.getString("album_type");
                        String artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name");
                        String external_ids = albumObj.getJSONObject("external_ids").getString("upc");
                        String external_urls = albumObj.getJSONObject("external_urls").getString("spotify");
                        String href = albumObj.getString("href");
                        String id = albumObj.getString("id");
                        String imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url");
                        String label = albumObj.getString("label");
                        String name = albumObj.getString("name");
                        int popularity = albumObj.getInt("popularity");
                        String release_date = albumObj.getString("release_date");
                        int total_tracks = albumObj.getInt("total_tracks");
                        String type = albumObj.getString("type");
                        // on below line adding data to array list.
                        albumRVModalArrayList.add(new AlbumRVModal(album_type, artistName, external_ids, external_urls, href, id, imgUrl, label, name, popularity, release_date, total_tracks, type));
                    }
                    albumRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get data : " + error, Toast.LENGTH_SHORT).show();
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
        // on below line adding request to queue.
        queue.add(albumObjReq);
    }

    private void initializePopularAlbumsRV() {
        // on below line creating list, initializing
        // adapter and setting it to recycler view.
        RecyclerView albumsRV = findViewById(R.id.idRVPopularAlbums);
        ArrayList<AlbumRVModal> albumRVModalArrayList = new ArrayList<>();
        AlbumRVAdapter albumRVAdapter = new AlbumRVAdapter(albumRVModalArrayList, this);
        albumsRV.setAdapter(albumRVAdapter);
        // on below line creating a variable for url
        String url = "https://api.spotify.com/v1/albums?ids=3jXbdginoAtjcBqT7GcYRd%2C1uD1kdwTWH1DZQZqGKz6rY";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // on below line making json object request to parse json data.
        JsonObjectRequest albumObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray albumArray = response.getJSONArray("albums");
                    for (int i = 0; i < albumArray.length(); i++) {
                        JSONObject albumObj = albumArray.getJSONObject(i);
                        String album_type = albumObj.getString("album_type");
                        String artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name");
                        String external_ids = albumObj.getJSONObject("external_ids").getString("upc");
                        String external_urls = albumObj.getJSONObject("external_urls").getString("spotify");
                        String href = albumObj.getString("href");
                        String id = albumObj.getString("id");
                        String imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url");
                        String label = albumObj.getString("label");
                        String name = albumObj.getString("name");
                        int popularity = albumObj.getInt("popularity");
                        String release_date = albumObj.getString("release_date");
                        int total_tracks = albumObj.getInt("total_tracks");
                        String type = albumObj.getString("type");
                        // on below line adding data to array list.
                        albumRVModalArrayList.add(new AlbumRVModal(album_type, artistName, external_ids, external_urls, href, id, imgUrl, label, name, popularity, release_date, total_tracks, type));
                    }
                    albumRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get data : " + error, Toast.LENGTH_SHORT).show();
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
        // on below line adding request to queue.
        queue.add(albumObjReq);
    }

    private void initializeTrendingAlbumsRV() {
        // on below line creating list, initializing adapter
        // and setting it to recycler view.
        RecyclerView albumsRV = findViewById(R.id.idRVTrendingAlbums);
        ArrayList<AlbumRVModal> albumRVModalArrayList = new ArrayList<>();
        AlbumRVAdapter albumRVAdapter = new AlbumRVAdapter(albumRVModalArrayList, this);
        albumsRV.setAdapter(albumRVAdapter);
        // on below line creating a variable for url
        String url = "https://api.spotify.com/v1/albums?ids=5HOHne1wzItQlIYmLXLYfZ%2C3AafSrFIbJPH6BJHiJm1Cd%2C7hhxms8KCwlQCWffIJpN9b%2C3umvKIjsD484pa9pCyPK2x%2C3OHC6XD29wXWADtAOP2geV%2C3RZxrS2dDZlbsYtMRM89v8%2C24C47633GRlozws7WBth7t";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // on below line making json object request to parse json data.
        JsonObjectRequest albumObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray albumArray = response.getJSONArray("albums");
                    for (int i = 0; i < albumArray.length(); i++) {
                        JSONObject albumObj = albumArray.getJSONObject(i);
                        String album_type = albumObj.getString("album_type");
                        String artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name");
                        String external_ids = albumObj.getJSONObject("external_ids").getString("upc");
                        String external_urls = albumObj.getJSONObject("external_urls").getString("spotify");
                        String href = albumObj.getString("href");
                        String id = albumObj.getString("id");
                        String imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url");
                        String label = albumObj.getString("label");
                        String name = albumObj.getString("name");
                        int popularity = albumObj.getInt("popularity");
                        String release_date = albumObj.getString("release_date");
                        int total_tracks = albumObj.getInt("total_tracks");
                        String type = albumObj.getString("type");
                        // on below line adding data to array list.
                        albumRVModalArrayList.add(new AlbumRVModal(album_type, artistName, external_ids, external_urls, href, id, imgUrl, label, name, popularity, release_date, total_tracks, type));
                    }
                    albumRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get data : " + error, Toast.LENGTH_SHORT).show();
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
        // on below line adding request to queue.
        queue.add(albumObjReq);
    }

}