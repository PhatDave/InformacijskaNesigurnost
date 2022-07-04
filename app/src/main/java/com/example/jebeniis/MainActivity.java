package com.example.jebeniis;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public final class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    EditText searchBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, String> questions = getQuestions();
        ArrayList<String> keyList = new ArrayList<>(questions.keySet());

        searchBox = findViewById(R.id.searchBox);
        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            String search = searchBox.getText().toString();
            List<FilterItem> filteredKeys = new ArrayList<>();

            for (String key : keyList) {
                key = key.toLowerCase();
                Integer minLev = 10000;
                String[] words = key.split(" ");
                for (String word : words) {
                    int lev = StringUtils.getLevenshteinDistance(word, search);
                    if (lev < minLev) {
                        minLev = lev;
                    }
                }
                if (minLev < 8) {
                    filteredKeys.add(new FilterItem(key, minLev));
                }
            }

            filteredKeys.sort((o1, o2) -> o1.lev - o2.lev);
            keyList.removeIf(key -> true);
            for (FilterItem item : filteredKeys) {
                keyList.add(item.key);
            }
            adapter.notifyDataSetChanged();
            return true;
        });

        adapter = new Adapter(this, keyList, questions);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public HashMap<String, String> getQuestions() {
        String content = loadAsset("is.json");
        HashMap<String, String> questions = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(content);
            jsonObject.keys().forEachRemaining(key -> {
                try {
                    String value = jsonObject.getString(key);
                    key = key.toLowerCase();
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