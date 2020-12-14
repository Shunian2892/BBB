package com.example.bbb.controlLayer.gps;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenRouteService {
    private OkHttpClient client;
    private String ipAddress;
    private int port;
    public boolean isConnected;

    private final String api_key = "5b3ce3597851110001cf6248cc7335a16be74902905bcba4a9d0eebf";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OpenRouteService() {
        this.client = new OkHttpClient();
        this.ipAddress = "localhost";
        this.port = 8000;
        this.isConnected = false;

        Connect();
    }

    private void Connect() {
    this.isConnected = true;
    }

    private Request createGetRequest(String method, String url) {
        Request request = new Request.Builder().url("\n" +
                "https://api.openrouteservice.org/v2/directions/" + method + "?api_key=" + this.api_key + url).build();
        return request;
    }

//    private Request createPostRequest(String url, String json) {
//        RequestBody requestBody = RequestBody.create(json, JSON);
//        Request request = new Request.Builder().url("http://" + this.ipAddress + ":" + this.port + "/api/" + this.username + url).post(requestBody).build();
//        return request;
//    }
//
//    private Request createPutRequest(String url, String json) {
//        RequestBody requestBody = RequestBody.create(json, JSON);
//        Request request = new Request.Builder().url("http://" + this.ipAddress + ":" + this.port + "/api/" + this.username + url).put(requestBody).build();
//        return request;
//    }
//
//    private Request createDeleteRequest(String url) {
//        Request request = new Request.Builder().url("http://" + this.ipAddress + ":" + this.port + "/api/" + this.username + url).delete().build();
//        return request;
//    }

    public List<GeoPoint> getRoute(GeoPoint startPoint, GeoPoint endPoint, String method) {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        if (this.isConnected) {
            client.newCall(createGetRequest(method,
                    "&start=" + startPoint.getLatitude() + "," + startPoint.getLongitude()
                            + "&end=" + endPoint.getLatitude() + "," + endPoint.getLongitude()))
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("FAILURE", "In OnFailure() in example()");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
                                JSONObject responseObject = new JSONObject(response.body().string());
                                JSONObject features = responseObject.getJSONObject("features");
                                JSONObject geometry = features.getJSONObject("geometry");
                                JSONArray coordinates = geometry.getJSONArray("coordinates");

                                for (int i = 0; i < coordinates.length(); i++) {
//                                    geoPoints.add(coordinates.get(i));
                                    System.out.println("COORDINATE " + i + ": " + coordinates.get(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return geoPoints;
    }




}

