package com.example.ad_spotifyclone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "edd544d18c2044c5a53faaedc9ffea15";
    private static final String CLIENT_SECRET = "4075da255de3448cb9c6f6821a29422b";
    private static final String REDIRECT_URI = "https://localhost:8080/callback";
    private static final String AUTH_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    private static final String BASE_URL = "https://accounts.spotify.com/api/token";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE_VALUE = "client_credentials";
    private static final String REFRESH_TOKEN_PARAM = "refresh_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private List<CategoryItem> categoryItems = new ArrayList<>();
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d("SearchLog", "SearchActivity onCreate called");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new CategoryAdapter(categoryItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d("AdapterLog", "Adapter is called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                generateOrRefreshToken();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        generateOrRefreshToken();
    }

    private void generateOrRefreshToken() {
        String accessToken = getAccessToken();

        if (accessToken != null) {
            fetchCategories();
        } else {
            generateToken();
        }
    }

    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    private String getRefreshToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(ACCESS_TOKEN_KEY, accessToken);
        myEdit.apply();
    }

    private void saveRefreshToken(String refreshToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(REFRESH_TOKEN_KEY, refreshToken);
        myEdit.apply();
    }

    private void generateToken() {
        String url = BASE_URL;

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String accessToken = jsonObject.getString("access_token");
                    saveAccessToken(accessToken);
                    fetchCategories();
                } catch (JSONException e) {
                    Log.e("generateToken", "JSON parsing error", e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("generateToken", "Volley error", error);
                Toast.makeText(SearchActivity.this, "Failed to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String authString = CLIENT_ID + ":" + CLIENT_SECRET;
                String base64Auth = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
                String authHeaderValue = "Basic " + base64Auth;
                headers.put(AUTH_HEADER, authHeaderValue);
                headers.put(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(GRANT_TYPE_PARAM, GRANT_TYPE_VALUE);
                return params;
            }
        };
        queue.add(request);
    }

    private void fetchCategories() {
        String token = getAccessToken();
        if (token == null) {
            Log.d("getAccessToken", "Access token is null");
            return;
        }

        String url = "https://api.spotify.com/v1/browse/categories";

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("fetchCategories", "Response: " + response);
                            JSONObject categoriesObject = response.getJSONObject("categories");
                            JSONArray itemsArray = categoriesObject.getJSONArray("items");
                            categoryItems.clear();

                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject categoryObject = itemsArray.getJSONObject(i);
                                String categoryName = categoryObject.getString("name");
                                categoryItems.add(new CategoryItem(categoryName));
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Log.e("fetchCategories", "Volley error", error);
                Toast.makeText(SearchActivity.this, "Failed to fetch categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, "Bearer " + token); // Use the access token for authorization
                return headers;
            }
        };
        queue.add(request);
    }
}
