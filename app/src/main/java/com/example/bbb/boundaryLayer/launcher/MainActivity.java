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
                Database.class, "database-test1.6").allowMainThreadQueries().build();

        //clear current tables
//        db.poiDao().deleteTable();
//        db.routeDao().deleteTable();
//        db.poi_route_Dao().deleteTable();

        //add poi's from Json
//        ArrayList<POI> poiList = new ArrayList<>();
        JSONArray jsonArray = readJson(R.raw.poi_file);
//
//        for (int i = 0; i < jsonArray.length(); i++){
//            JSONObject jsonObject = null;
//            POI poi = new POI();
//            try {
//                jsonObject = (JSONObject) jsonArray.get(i);
//                poi.ID = jsonObject.getInt("id");
//                poi.POIName = jsonObject.getString("name");
//                poi.longitude = jsonObject.getDouble("longitude");
//                poi.latitude = jsonObject.getDouble("latitude");
//                poi.Description = jsonObject.getString("description");
//                poi.imageURL = jsonObject.getString("imageUrl");
//                poi.VideoURL = jsonObject.getString("videoUrl");
//                poi.IsVisited = jsonObject.getBoolean("isVisited");
//                poiList.add(poi);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        db.poiDao().insertAll(poiList);
//
//        //add route
//        Route route = new Route();
//        route.ID = 1;
//        route.RouteName = "Historisch";
//        db.routeDao().insert(route);
//
//        //add poi's to route
//        for (int i = 0; i < 4; i++) {
//            POI_Route poi_route = new POI_Route();
//            poi_route.PoiID = i + 1;
//            poi_route.RouteID = 1;
//            db.poi_route_Dao().insert(poi_route);
//        }

        System.out.println("Size of routes: " + db.routeDao().getAll().size());
        System.out.println("Size of join: " + db.poi_route_Dao().getAll().size());
        System.out.println("size of poi: " + db.routeDao().getPOIs(1).get(0).POIName);

//        for (int i = 0; i < db.routeDao().getPOIs(1).size(); i++) {
//            System.out.println("Value: " + db.routeDao().getPOIs(1).get(i).RouteID + "   " + db.routeDao().getPOIs(1).get(0).PoiID);
//        }
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
}