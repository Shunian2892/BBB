package com.example.bbb.controlLayer.gps;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.bbb.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class OpenStreetMaps {

    public void drawRoute(MapView mapView, ArrayList<GeoPoint> geoPoints) {
        Polyline line = new Polyline();
        line.setPoints(geoPoints);
        line.setColor(Color.parseColor("#FFA400"));
        mapView.getOverlayManager().add(line);
    }


    public void drawMarker(MapView mapView, GeoPoint point, Drawable icon) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }

}
