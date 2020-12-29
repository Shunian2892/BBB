package com.example.bbb.controlLayer.geofencing;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.util.GeoPoint;

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

    public void setupGeoFencing() {
        checkFineLocationPermission();


        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFenceHelper = new GeoFenceHelper(context);

        if (Build.VERSION.SDK_INT >= 29) {
            //If API is higher then 29 we need background permission

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                addFences();
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
            addFences();
        }


    }

    private void addFences() {

        GeoPoint monkeyTown = new GeoPoint(51.59055720605067, 4.765112269496669);
        addGeoFence(monkeyTown, 200, monkeyTown.toString());

        GeoPoint brug = new GeoPoint(51.59244308378478, 4.7700728786915185);
        addGeoFence(brug, 200, monkeyTown.toString());

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


    private void checkFineLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
}
