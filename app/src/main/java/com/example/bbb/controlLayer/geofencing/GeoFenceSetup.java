package com.example.bbb.controlLayer.geofencing;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bbb.entityLayer.data.POI;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GeoFenceSetup {

    private Context context;
    private Activity appActivity;
    private GeofencingClient geofencingClient;
    private GeoFenceHelper geoFenceHelper;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    public GeoFenceSetup(Context constrcontext, Activity appActivity) {
        this.context = constrcontext;
        this.appActivity = appActivity;

    }

    public void setupGeoFencing(List<POI> poiList) {
        checkFineLocationPermission();


        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFenceHelper = new GeoFenceHelper(context);

        if (Build.VERSION.SDK_INT >= 29) {
            //If API is higher then 29 we need background permission

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                addFences(poiList);
            } else {
                //Permission is not granted!! Need to request it..
                if (ActivityCompat.shouldShowRequestPermissionRationale(appActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(appActivity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(appActivity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);

                }
            }
        } else {
            addFences(poiList);
        }


    }

    private void addFences(List<POI> poiList) {

        System.out.println(":::::" + poiList.size());

        for (int i = 0; i < poiList.size(); i++) {
            System.out.println(poiList.get(i).longitude + "---" + poiList.get(i).latitude + "---" + poiList.get(i).POIName);
            GeoPoint tempPoint = new GeoPoint(poiList.get(i).longitude, poiList.get(i).latitude);
            addGeoFence(tempPoint, 45, poiList.get(i).POIName);

        }
    }

    private void addGeoFence(GeoPoint geoPoint, float radius, String ID) {
        checkFineLocationPermission();

        Geofence geofence = geoFenceHelper.getGeofence(ID, geoPoint, radius,
                Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

        GeofencingRequest geofencingRequest = geoFenceHelper.getGeoFencingRequest(geofence);
        PendingIntent pendingIntent = geoFenceHelper.getPendingIntent();


        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v(TAG, "Geofence is added... ");
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(TAG, e.getLocalizedMessage());
            }


        });
    }

    public void removeGeoFences(List<POI> poiList) {
        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFenceHelper = new GeoFenceHelper(context);
        PendingIntent pendingIntent = geoFenceHelper.getPendingIntent();


        List <String> poiNameList = new ArrayList<>();

        for (int i = 0; i < poiList.size(); i++) {
            poiNameList.add(poiList.get(i).POIName);
        }


        System.out.println("@@@@@@@@@@@@@@" + poiNameList.size());

        geofencingClient.removeGeofences(pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v(TAG, "Geofence is removed... ");
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(TAG, e.getLocalizedMessage());
            }


        });

        poiNameList.clear();
    }


    private void checkFineLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
}
