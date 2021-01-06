package com.example.bbb.controlLayer;

import androidx.room.Room;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.App;
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
import java.util.List;

public class DatabaseManager {

    private static DatabaseManager instance = null;

    synchronized public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private final Database db;

    private DatabaseManager() {
        db = Room.databaseBuilder(App.getContext(), Database.class, "database-test3.0").allowMainThreadQueries().build();
        initDatabase();
    }

    public void initDatabase() {
        //add poi's from Json

        if (db.poiDao().getAll().size() == 0) {

            ArrayList<POI> poiList = new ArrayList<>();
            JSONArray jsonArrayPOI = readJson(R.raw.poi_file);

            for (int i = 0; i < jsonArrayPOI.length(); i++) {
                JSONObject jsonObject;
                POI poi = new POI();
                try {
                    jsonObject = jsonArrayPOI.getJSONObject(i);
                    poi.ID = jsonObject.getInt("id");
                    poi.POIName = jsonObject.getString("name");
                    poi.longitude = convertDMStoDD(jsonObject.getString("longitude"));
                    poi.latitude = convertDMStoDD(jsonObject.getString("latitude"));
                    poi.Description_nl = jsonObject.getString("description nl");
                    poi.Description_en = jsonObject.getString("description en");
                    poi.Description_fr = jsonObject.getString("description fr");
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
                    route.RouteName_nl = jsonObject.getString("name nl");
                    route.RouteName_en = jsonObject.getString("name en");
                    route.RouteName_fr = jsonObject.getString("name fr");
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

    private double convertDMStoDD(String point) {
        int degrees = Integer.parseInt(point.substring(0, point.indexOf("*")));
        double minutes = Double.parseDouble(point.substring(point.indexOf("*") + 1));
        return degrees + (minutes / 60);
    }

/*    public Database getDB(){
        return db;
    }*/

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
            InputStream inputStream = App.getContext().getResources().openRawResource(file);
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
    }

    public List<POI> getPOIs() {
        return db.poiDao().getAll();
    }

    public List<Route> getRoutes() {
        return db.routeDao().getAll();
    }

    public Route getRoute(int ID) {
        return db.routeDao().getRoute(ID);
    }

    public List<POI> getPOIsFromRoute(int routeID) {
        return db.routeDao().getPOIs(routeID);
    }

    public void addWalkedRoute(int routeID, String date) {
        WalkedRoute walkedRoute = new WalkedRoute();
        walkedRoute.date = date;
        walkedRoute.routeID = routeID;
        db.walked_route_Dao().insert(walkedRoute);
    }

    public List<WalkedRoute> getWalkedRoutes() {
        return db.walked_route_Dao().getAll();
    }

    public List<POI> searchLocation(String name) {
        return db.poiDao().matchedPOIs(name);
    }
}
