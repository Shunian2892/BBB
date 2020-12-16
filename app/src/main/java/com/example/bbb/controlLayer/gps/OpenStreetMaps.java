package com.example.bbb.controlLayer.gps;

import android.graphics.drawable.Drawable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class OpenStreetMaps {

    public void drawRoute(MapView mapView, ArrayList<GeoPoint> geoPoints) {
        Polyline line = new Polyline();
        line.setPoints(geoPoints);
        mapView.getOverlayManager().add(line);
    }


    public void drawMarker(MapView mapView, GeoPoint point){
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        //marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }

}
