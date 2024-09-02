package com.example.ispit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button buttonJoke;
    Button buttonSettings;
    Button buttonFavorite;
    Button buttonFavorites;

    boolean allowJoke = false;
    boolean alreadyFavorite = false;

    //  -------------------  JSON FAVORITES STUFF  ------------
    public String jokeId;
    public String joke;
    JSONObject currentResponse;
    JSONArray jokeList = new JSONArray();

    private DB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB(getFilesDir());
        jokeList = db.readAll();
        buttonJoke = (Button) findViewById(R.id.buttonJoke);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);

        SharedPreferences preference;
        preference = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        if (preference.getBoolean("night", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }


        String url = "https://icanhazdadjoke.com/";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        textView = (TextView) findViewById(R.id.textViewJoke);

                        try {
                            textView.setText(response.getString("joke"));
                            jokeId = response.getString("id");
                            joke = response.getString("joke");
                            currentResponse = response;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("Accept", "application/json");
                return header;
            }
        };


        buttonJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue.add(jsonObjectRequest);
                allowJoke = true;
                alreadyFavorite = false;

            }
        });


        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });


// --------------------------- ADD STUFF TO JSON -----------------------
        buttonFavorite = (Button) findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowJoke == true && alreadyFavorite == false) {
                    alreadyFavorite = true;
                    db.add(currentResponse);
                }
            }
        });

        buttonFavorites = (Button) findViewById(R.id.buttonFavorites);
        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFavorites();
            }
        });


    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openFavorites() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }
}

