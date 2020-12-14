package com.example.bbb.boundaryLayer.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.RoutePopUp;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.POI_Route;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new RoutePopUp();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });
        db = Room.databaseBuilder(getApplicationContext(), Database.class, "database-test2.1").allowMainThreadQueries().build();
        //initDatabase();
        testQueries();
    }

    public void initDatabase(){
        //clear current tables

        //add poi's from Json
        ArrayList<POI> poiList = new ArrayList<>();
        JSONArray jsonArrayPOI = readJson(R.raw.poi_file);

        for (int i = 0; i < jsonArrayPOI.length(); i++){
            JSONObject jsonObject = null;
            POI poi = new POI();
            try {
                jsonObject = jsonArrayPOI.getJSONObject(i);
                poi.ID = jsonObject.getInt("id");
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
        db.poiDao().insertAll(poiList);

        //add route
        ArrayList<Route> routeList = new ArrayList<>();
        JSONArray jsonArrayRoute = readJson(R.raw.route_file);

        for (int i = 0; i < jsonArrayRoute.length(); i++) {
            JSONObject jsonObject = null;
            Route route = new Route();
            try {
                jsonObject = jsonArrayRoute.getJSONObject(i);
                route.ID = jsonObject.getInt("id");
                route.RouteName = jsonObject.getString("name");
                routeList.add(route);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        db.routeDao().insertAll(routeList);


        //add poi's to route
        ArrayList<POI_Route> poi_routeList = new ArrayList<>();
        JSONArray jsonArrayPoiRoute = readJson(R.raw.poi_route_file);

        for (int i = 0; i < jsonArrayPoiRoute.length(); i++) {
            JSONObject jsonObject = null;
            POI_Route poi_route = new POI_Route();
            try {
                jsonObject = jsonArrayPoiRoute.getJSONObject(i);
                poi_route.RouteID = jsonObject.getInt("routeID");
                poi_route.PoiID = jsonObject.getInt("poiID");
                poi_routeList.add(poi_route);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        db.poi_route_Dao().insertAll(poi_routeList);

        System.out.println("Size of poi: " + db.poiDao().getAll().size());
        System.out.println("Size of routes: " + db.routeDao().getAll().size());
        System.out.println("Size of join: " + db.poi_route_Dao().getAll().size());
        System.out.println("size of poi: " + db.routeDao().getPOIs(1).get(0).POIName);
    }

    public JSONArray readJson(int file){
        JSONArray array = null;
        try {
             array = new JSONArray(loadJSONFromFile(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public String loadJSONFromFile(int file){
        String json = null;
        try {
            InputStream inputStream = getResources().openRawResource(file);
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

    public void testQueries(){
        System.out.println("Size of poi: " + db.poiDao().getAll().size()); //get POI amount
        System.out.println("Size of routes: " + db.routeDao().getAll().size()); //get route amount
        System.out.println("Size of join: " + db.poi_route_Dao().getAll().size()); //get join amount
        System.out.println("Name: " + db.routeDao().getPOIs(1).get(0).POIName); //get name of first POI of the first route
        System.out.println("Longitude: " + db.poiDao().matchedPOIs("Liefdeszuster").longitude); //get longitude of Liefdeszuster
    }
}