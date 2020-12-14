package com.example.bbb.entityLayer.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class POI {

    @NonNull
    @PrimaryKey
    public String POIName;

    @ColumnInfo
    public double longitude;

    @ColumnInfo
    public double latitude;

    @ColumnInfo
    public String Description;

    @ColumnInfo
    public String imageURL;

    @ColumnInfo
    public String VideoURL;

    @ColumnInfo
    public Boolean IsVisited;
}
