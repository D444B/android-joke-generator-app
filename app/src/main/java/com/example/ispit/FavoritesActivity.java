package com.example.ispit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {


    private DB db;

    // NEW
    SearchView searchView;
    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        db = new DB(getFilesDir());


        JSONArray jokeList = db.readAll();

        // NEW


        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<>();
        list.setLongClickable(true);
        list.setClickable(true);


        try {
            for (int i = 0; i < jokeList.length(); i++) {
                JSONObject joke = jokeList.getJSONObject(i);

                String jokeText = joke.getString("joke");
                arrayList.add(jokeText);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int listItem, long l) {

                new AlertDialog.Builder(FavoritesActivity.this)
                        .setTitle("Remove selected joke?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                arrayList.remove(listItem);
                                db.delete(listItem);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                }).create().show();

                return false;
            }

        });


        // Serach View
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FavoritesActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FavoritesActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }
}