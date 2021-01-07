package com.example.bbb.controlLayer.gps;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.example.bbb.boundaryLayer.ui.IMarkerClickListener;
import com.example.bbb.entityLayer.data.POI;

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
        mapView.invalidate();
    }

    public void drawMarker(MapView mapView, GeoPoint point,Drawable iconUnvisited, Drawable iconVisited, POI poi, IMarkerClickListener listener) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        if(poi.IsVisited){
            marker.setIcon(iconVisited);
        }else {
            marker.setIcon(iconUnvisited);
        }
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                listener.onMarkerClicked(poi);
                return false;
            }
        });
        mapView.getOverlays().add(marker);
    }

    public void drawMarker(MapView mapView, GeoPoint point,Drawable icon, POI poi, IMarkerClickListener listener) {

        if (mapView != null) {

            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(icon);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    listener.onMarkerClicked(poi);
                    return false;
                }
            });
            mapView.getOverlays().add(marker);
        }
    }

}
