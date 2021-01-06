package com.example.bbb.controlLayer.gps;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.BBBViewmodel;
import com.example.bbb.boundaryLayer.ui.IPOIVistitedListener;
import com.example.bbb.boundaryLayer.ui.MarkerClickListener;
import com.example.bbb.boundaryLayer.ui.POIFragment;
import com.example.bbb.boundaryLayer.ui.UIViewModel;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenRouteService implements MarkerClickListener, IPOIVistitedListener {
    private OkHttpClient client;
    private String ipAddress;
    private int port;
    public boolean isConnected;

    private OpenStreetMaps openStreetMaps;
    private MapView mapView;
    private View view;
    private Context context;
    private UIViewModel viewModel;
    private FragmentManager manager;

    private final String api_key = "5b3ce3597851110001cf6248cc7335a16be74902905bcba4a9d0eebf";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OpenRouteService(MapView mapView, Context context, View view, UIViewModel viewModel, FragmentManager manager) {
        this.client = new OkHttpClient();
        this.ipAddress = "localhost";
        this.port = 8000;
        this.isConnected = false;
        this.openStreetMaps = new OpenStreetMaps();
        this.mapView = mapView;
        this.view = view;
        this.context = context;
        this.viewModel = viewModel;
        this.manager = manager;

        BBBViewmodel.getInstance().setIpoiVistitedListener(this);

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

    private Request createPostRequest(String method, String json) {
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url("https://api.openrouteservice.org/v2/directions/" + method).
                post(requestBody).addHeader("Authorization", api_key).build();
        return request;
    }

    public void getRoute(GeoPoint startPoint, GeoPoint endPoint, String method) {
        ArrayList<GeoPoint> points = new ArrayList<>();

        if (this.isConnected) {
            client.newCall(createGetRequest(method,
                    "&start=" + startPoint.getLongitude() + "," + startPoint.getLatitude()
                            + "&end=" + endPoint.getLongitude() + "," + endPoint.getLatitude()))
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("FAILURE", "In OnFailure() in example()");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
                                JSONObject responseObject = new JSONObject(response.body().string());
                                JSONArray featureArray = responseObject.getJSONArray("features");
                                JSONObject feature = (JSONObject) featureArray.get(0);
                                JSONObject geometry = feature.getJSONObject("geometry");
                                JSONArray coordinates = geometry.getJSONArray("coordinates");

                                for (int i = 0; i < coordinates.length(); i++) {
                                    JSONArray coordArray = (JSONArray) coordinates.get(i);
                                    double lng = coordArray.getDouble(0);
                                    double lat = coordArray.getDouble(1);
                                    GeoPoint point = new GeoPoint(lat, lng);
                                    points.add(point);
                                }

                                if (view == null) {
                                    return;
                                }
                                openStreetMaps.drawRoute(mapView, points);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public void getRoute(double[][] waypoints, String method, String language, Route route) {
        if (this.isConnected) {
            ArrayList<GeoPoint> points = new ArrayList<>();

            client.newCall(createPostRequest(method, "{\"coordinates\":" + Arrays.deepToString(waypoints) + ",\"language\":\"" + language + "\"}"))
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("FAILURE", "In OnFailure() in getRoute() multiple");
                        }

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
//                                System.out.println(response.body().string());
                                JSONObject responseObject = new JSONObject(response.body().string());
                                JSONArray routesArray = responseObject.getJSONArray("routes");
                                JSONObject routes = (JSONObject) routesArray.get(0);
                                String geometry = routes.getString("geometry");
                                JSONArray coordinates = decodeGeometry(geometry, false);


                                for (int i = 0; i < coordinates.length(); i++) {
                                    JSONArray cordArray = (JSONArray) coordinates.get(i);
                                    double lat = cordArray.getDouble(0);
                                    double lng = cordArray.getDouble(1);
                                    GeoPoint point = new GeoPoint(lat, lng);
                                    points.add(point);
                                }


                                for (int i = 0; i < waypoints.length; i++) {
                                    if (view == null) {
                                        return;
                                    }
                                    if (i != 0 && i != waypoints.length - 1) {
                                        openStreetMaps.drawMarker(
                                                mapView, new GeoPoint(waypoints[i][1], waypoints[i][0]),
                                                context.getDrawable(R.drawable.waypoint_unvisited), context.getDrawable(R.drawable.waypoint_visited), DatabaseManager.getInstance(context).getPOIsFromRoute(route.ID).get(i), OpenRouteService.this);
                                    } else if (i == 0) {
                                        openStreetMaps.drawMarker(
                                                mapView, new GeoPoint(waypoints[i][1], waypoints[i][0]),
                                                context.getDrawable(R.drawable.start_location), DatabaseManager.getInstance(context).getPOIsFromRoute(route.ID).get(i), OpenRouteService.this);
                                    } else {
                                        openStreetMaps.drawMarker(
                                                mapView, new GeoPoint(waypoints[i][1], waypoints[i][0]),
                                                context.getDrawable(R.drawable.end_location), DatabaseManager.getInstance(context).getPOIsFromRoute(route.ID).get(i), OpenRouteService.this);
                                    }

                                }

                                openStreetMaps.drawRoute(mapView, points);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private static JSONArray decodeGeometry(String encodedGeometry, boolean inclElevation) {
        JSONArray geometry = new JSONArray();
        int len = encodedGeometry.length();
        int index = 0;
        int lat = 0;
        int lng = 0;
        int ele = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedGeometry.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);


            if (inclElevation) {
                result = 1;
                shift = 0;
                do {
                    b = encodedGeometry.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                ele += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            }

            JSONArray location = new JSONArray();
            try {
                location.put(lat / 1E5);
                location.put(lng / 1E5);
                if (inclElevation) {
                    location.put((float) (ele / 100));
                }
                geometry.put(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return geometry;
    }

    @Override
    public void onMarkerClicked(POI poi) {
        viewModel.setSelectedPOI(poi);
        manager.beginTransaction().replace(R.id.fragment_container, new POIFragment()).addToBackStack(null).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPoiIsVisited(POI poi) {
        openStreetMaps.drawMarker(mapView,new GeoPoint(poi.longitude, poi.latitude),context.getDrawable(R.drawable.waypoint_visited),poi,this);
        System.out.println("@@@@@@@ on POI is Visited");
    }
}

