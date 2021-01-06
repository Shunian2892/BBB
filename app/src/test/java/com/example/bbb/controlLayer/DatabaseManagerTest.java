package com.example.bbb.controlLayer;

import android.content.Context;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.App;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//Difficult to test. Mockito can't test a class with a static instance.
//Don't know how to test the database.
public class DatabaseManagerTest {

    @Mock
    private DatabaseManager databaseManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseManager = mock(DatabaseManager.class);
    }

    @Test
    public void getInstance() {
        setMock(databaseManager);
        DatabaseManager databaseManagerActual = DatabaseManager.getInstance(App.getContext());
        System.out.println(databaseManagerActual);
        assertNotNull(databaseManagerActual);
    }

    private void setMock(DatabaseManager mock) {
        try {
            Field instance = DatabaseManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Doesn't work. Don't know how to mock a singleton.
//    @Test
//    public void initDatabase() {
//        //actual values
//        databaseManager.initDatabase();
//        Database database = databaseManager.getDB();
//        List<POI> pois = database.poiDao().getAll();
//
//        //expected values
//        ArrayList<POI> poiList = new ArrayList<>();
//        JSONArray jsonArrayPOI = readJson(R.raw.poi_file);
//        for (int i = 0; i < jsonArrayPOI.length(); i++) {
//            JSONObject jsonObject = null;
//            POI poi = new POI();
//            try {
//                jsonObject = jsonArrayPOI.getJSONObject(i);
//                poi.ID = jsonObject.getInt("id");
//                poi.POIName = jsonObject.getString("name");
//                poi.longitude = convertDMStoDD(jsonObject.getString("longitude"));
//                poi.latitude = convertDMStoDD(jsonObject.getString("latitude"));
//                poi.Description = jsonObject.getString("description");
//                poi.imageURL = jsonObject.getString("imageUrl");
//                poi.VideoURL = jsonObject.getString("videoUrl");
//                poi.IsVisited = jsonObject.getBoolean("isVisited");
//                poiList.add(poi);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        assertEquals(poiList.size(),pois.size());
//    }
//
//    private double convertDMStoDD(String point){
//        int degrees = Integer.parseInt(point.substring(0,point.indexOf("*")));
//        double minutes = Double.parseDouble(point.substring(point.indexOf("*")+1));
//
//        return degrees+(minutes/60);
//    }


/*    public JSONArray readJson(int file) {
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
    }*/

    /*private final int file = R.raw.route_file;

    @Test
    public void readJsonTest() {

    }

    @Test
    public void loadJSONFromFileTest() {
    }

    @Test
    public void testQueries() {
    }

    @Test
    public void getPOIs() {
    }

    @Test
    public void getRoutes() {
    }

    @Test
    public void getPOIsFromRoute() {
    }

    @Test
    public void addWalkedRoute() {
    }

    @Test
    public void getWalkedRoutes() {
    }

    @Test
    public void searchLocation() {
    }*/
}