package com.example.jebeniis;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, String> questions = getQuestions();
        System.out.println(questions);
    }

    public HashMap<String, String> getQuestions() {
        String content = loadAsset("is.json");
        HashMap<String, String> questions = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(content);
            jsonObject.keys().forEachRemaining(key -> {
                try {
                    String value = jsonObject.getString(key);
                    questions.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            return questions;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadAsset(String asset) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(asset);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}