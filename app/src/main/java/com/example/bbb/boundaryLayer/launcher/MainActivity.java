package com.example.bbb.boundaryLayer.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.HelpPopUp;
import com.example.bbb.boundaryLayer.ui.RoutePopUp;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new RoutePopUp();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });
        testDatabase();
    }

    public void testDatabase(){
        Database db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "database-test1.0").allowMainThreadQueries().build();

        db.poiDao().deleteTable();
        ArrayList<POI> poiList = new ArrayList<>();
        JSONArray jsonArray = readJson();

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = null;
            POI poi = new POI();
            try {
                jsonObject = (JSONObject) jsonArray.get(i);
                poi.POIName = jsonObject.getString("name");
                poi.longitude = jsonObject.getDouble("longitude");
                poi.latitude = jsonObject.getDouble("latitude");
                poi.Description = jsonObject.getString("description");
                poi.imageURL = jsonObject.getString("imageUrl");
                poi.VideoURL = jsonObject.getString("videoUrl");
                poi.IsVisited = jsonObject.getBoolean("isVisited");
                poiList.add(poi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        ArrayList<POI> poiList = new ArrayList<>();
//        for (int i = 0; i < 4; i++){
//            POI poi = new POI();
//            poi.POIName = "Test name " + i;
//            poi.longitude = 0.0;
//            poi.latitude = 0.0;
//            poi.Description = "Test description " + i;
//            poi.imageURL = "Test imageURL " + i;
//            poi.VideoURL = "Test videoURL " + i;
//            poi.IsVisited = false;
//            poiList.add(poi);
//        }

        db.poiDao().insertAll(poiList);

        System.out.println("Size of databaselist: " + db.poiDao().getAll().size());
    }

    public JSONArray readJson(){
        JSONArray array = null;
        try {
             array = new JSONArray(loadJSONFromFile());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public String loadJSONFromFile(){
        String json = null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.poi_file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}