package com.example.bbb.controlLayer.gps;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class OpenStreetMaps {

    private List<GeoPoint> geoPoints;

    public OpenStreetMaps(){
        this.geoPoints = new ArrayList<>();
        init();
    }

    public void init(){
        geoPoints.add(new GeoPoint(51.5897, 4.7616));
        geoPoints.add(new GeoPoint(51.5890, 4.7758));
        geoPoints.add(new GeoPoint(51.5957, 4.7795));
        geoPoints.add(new GeoPoint(51.5859, 4.7924));
    }

    public void drawRoute(MapView mapView){
        Polyline line = new Polyline();
        line.setPoints(this.geoPoints);
        mapView.getOverlayManager().add(line);
    }

}
