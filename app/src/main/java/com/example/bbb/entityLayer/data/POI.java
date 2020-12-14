package com.example.bbb.entityLayer.data;

import android.media.Image;

public class POI {
    private String name;
    private String description;
    private String place;
    private Image picture;
    private boolean isVisited;

    public POI(String name, String description, String place, Image picture, boolean isVisited) {
        this.name = name;
        this.description = description;
        this.place = place;
        this.picture = picture;
        this.isVisited = isVisited;
    }
}
