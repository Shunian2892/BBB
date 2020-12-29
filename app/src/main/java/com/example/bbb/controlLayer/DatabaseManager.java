package com.example.bbb.controlLayer;

import android.content.Context;

import androidx.room.Room;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.POI_Route;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;
import com.example.bbb.entityLayer.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

    public static DatabaseManager instance = null;
    synchronized public static DatabaseManager getInstance(Context context){
        if (instance == null){
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    Database db;
    Context mainContext;
    
    public DatabaseManager(Context applicationContext) {
        mainContext = applicationContext;
        db = Room.databaseBuilder(applicationContext, Database.class, "database-test2.3").allowMainThreadQueries().build();
    }

    public void initDatabase() {
        //add poi's from Json
        if (db.poiDao().getAll().size() == 0) {

            ArrayList<POI> poiList = new ArrayList<>();
            JSONArray jsonArrayPOI = readJson(R.raw.poi_file);
            for (int i = 0; i < jsonArrayPOI.length(); i++) {
                JSONObject jsonObject = null;
                POI poi = new POI();
                try {
                    jsonObject = jsonArrayPOI.getJSONObject(i);
                    poi.ID = jsonObject.getInt("id");
                    poi.POIName = jsonObject.getString("name");
                    poi.longitude = convertDMStoDD(jsonObject.getString("longitude"));
                    poi.latitude = convertDMStoDD(jsonObject.getString("latitude"));
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

        }


        if (db.routeDao().getAll().size() == 0) {

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

        }


        if (db.poi_route_Dao().getAll().size() == 0) {

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


        }
    }

    private double convertDMStoDD(String point){
        int degrees = Integer.parseInt(point.substring(0,point.indexOf("*")));
        double minutes = Double.parseDouble(point.substring(point.indexOf("*")+1));

        return degrees+(minutes/60);
    }

    public JSONArray readJson(int file) {
        JSONArray array = null;
        try {
            array = new JSONArray(loadJSONFromFile(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public String loadJSONFromFile(int file) {
        String json = null;
        try {
            InputStream inputStream = mainContext.getResources().openRawResource(file);
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

    public void testQueries() {
        System.out.println("Size of poi: " + db.poiDao().getAll().size()); //get POI amount
        System.out.println("Size of routes: " + db.routeDao().getAll().size()); //get route amount
        System.out.println("Size of join: " + db.poi_route_Dao().getAll().size()); //get join amount
        System.out.println("Name: " + db.routeDao().getPOIs(1).get(0).POIName); //get name of first POI of the first route
        System.out.println("Longitude: " + db.poiDao().matchedPOIs("Liefdeszuster").longitude); //get longitude of Liefdeszuster
    }

    public List<POI> getPOIs() {
        List<POI> poiArrayList = new ArrayList<>();
        poiArrayList = db.poiDao().getAll();
        return poiArrayList;
    }

    public List<Route> getRoutes() {
        List<Route> routeList = new ArrayList<>();
        routeList = db.routeDao().getAll();
        return routeList;
    }

    public List<POI> getPOIsFromRoute(int routeID) {
        List<POI> poiList = db.routeDao().getPOIs(routeID);
        return poiList;
    }

    public void addWalkedRoute(int routeID, String date) {
        WalkedRoute walkedRoute = new WalkedRoute();
        walkedRoute.date = date;
        walkedRoute.routeID = routeID;
        db.walked_route_Dao().insert(walkedRoute);
    }

    public List<WalkedRoute> getWalkedRoutes() {
        List<WalkedRoute> walkedRouteList = db.walked_route_Dao().getAll();
        return walkedRouteList;
    }

    public POI searchLocation(String name) {
        POI poi = db.poiDao().matchedPOIs(name);
        return poi;
    }
}
