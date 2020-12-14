package com.example.bbb.entityLayer.database;

import androidx.room.RoomDatabase;

import com.example.bbb.entityLayer.data.POI;

@androidx.room.Database(entities = {POI.class}, version = 2)
public abstract class Database extends RoomDatabase {
    public abstract POIDao poiDao();
}
