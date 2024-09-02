package com.example.ispit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DB {

    private File filesDir;

    public DB(File filesDir) {
        this.filesDir = filesDir;
    }

    public JSONArray readAll() {
//        JSONArray jsonArray = null;
        JSONArray jsonArray = new JSONArray();

        File file = new File(filesDir, "jokeList.json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = br.readLine();
            }
            br.close();
            String response = stringBuilder.toString();
            jsonArray = new JSONArray(response);
            System.out.println("LOAD TEST " + jsonArray.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void add(JSONObject joke) {
        JSONArray jokeList = readAll();
        jokeList.put(joke);
        System.out.println(jokeList.toString());

        try {
            File file = new File(filesDir, "jokeList.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jokeList.toString());

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//                finally?
    }

    public void delete(int id) {
        {
            JSONArray jokeList = readAll();
            jokeList.remove(id);
            System.out.println(jokeList.toString());

            try {
                File file = new File(filesDir, "jokeList.json");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jokeList.toString());

                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                finally?
        }
    }

    public void update(JSONObject obj) {

    }

    public void deleteAll() {
        JSONArray jokeList = readAll();
        System.out.println(jokeList.toString());

        try {
            File file = new File(filesDir, "jokeList.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//                finally?
    }
}
