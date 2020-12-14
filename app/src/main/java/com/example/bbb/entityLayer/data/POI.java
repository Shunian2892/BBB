package com.example.bbb.entityLayer.data;

public class POI {
    private String name;
    private String latitude;
    private String longtitude;
    private String discription;
    private String imageUrl;
    private String videoUrl;
    private boolean isVisited;


    public POI(String name, String latitude, String longtitude, String discription){
        this.name = name;
        this.latitude = latitude;
        this.longtitude =longtitude;
        this.discription = discription;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public String toString() {
        return "POI{" +
                "name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", discription='" + discription + '\'' +
                '}';
    }
}
