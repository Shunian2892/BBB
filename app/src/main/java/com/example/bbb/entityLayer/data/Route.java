package com.example.bbb.entityLayer.data;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class Route {

    private String name;
    private List<POI> poiNames;
    private int totalDistanceWalked;
    private int timesWalked;
    private boolean isCompleted;
    private String startPoiName;
    private String endPoiName;
    private List<GeoPoint> geoPoints;

    public Route(String name, List<POI> poiNames, int totalDistanceWalked,
                 int timesWalked, boolean isCompleted, String startPoiName,
                 String endPoiName, List<GeoPoint> geoPoints) {
        this.name = name;
        this.poiNames = poiNames;
        this.totalDistanceWalked = totalDistanceWalked;
        this.timesWalked = timesWalked;
        this.isCompleted = isCompleted;
        this.startPoiName = startPoiName;
        this.endPoiName = endPoiName;
        this.geoPoints = geoPoints;
    }
}
