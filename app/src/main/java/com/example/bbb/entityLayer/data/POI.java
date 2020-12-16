package com.example.bbb.entityLayer.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class POI {

    @PrimaryKey
    public int ID;
    public String POIName;
    public double longitude;
    public double latitude;
    public String Description;
    public String imageURL;
    public String VideoURL;
    public Boolean IsVisited;
}
