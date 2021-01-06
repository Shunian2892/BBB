package com.example.bbb.controlLayer.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.boundaryLayer.ui.UIViewModel;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.gps.OpenStreetMaps;
import com.example.bbb.entityLayer.data.POI;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static android.content.ContentValues.TAG;

public class GeoFenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive BrCastReceiver Error ");
            return;
        }
        int transitionType = geofencingEvent.getGeofenceTransition();

        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
                String closebyPOIs = "";
                System.out.println("@@##@#@#@#@@#@# Triggered Geofences: ");
                for (int i = 0; i < geofenceList.size(); i++) {
                    String requestID =geofenceList.get(i).getRequestId();
                    System.out.println(requestID);
                    closebyPOIs += requestID + " ";

                    List<POI> pois= databaseManager.getPOIs();
                    for (POI poi:pois) {
                        if (poi.POIName.equals(requestID)){
                            poi.IsVisited = true;
                            databaseManager.changePOIState(poi);
                        }
                    }

                    for (POI poi:databaseManager.getPOIs()) {
                        System.out.println(poi.POIName + " " + poi.IsVisited);
                    }
                }

                Toast.makeText(context, "You are close to: " + closebyPOIs, Toast.LENGTH_LONG).show();
                Log.d(TAG, "GEOFENCE_TRANSITION_ENTER");
                break;


            case Geofence.GEOFENCE_TRANSITION_DWELL:
                //Toast.makeText(context, "You are walking around by: ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "GEOFENCE_TRANSITION_DWELL");
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
               //Toast.makeText(context, "You are leaving: ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "GEOFENCE_TRANSITION_EXIT");
                break;


        }
    }
}
