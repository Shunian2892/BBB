package com.example.bbb.entityLayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.POI_Route;

import java.util.List;

@Dao
public interface PoiRouteDao {

    @Query("SELECT * FROM POI_Route")
    List<POI_Route> getAll();

    //add new queries here
    //@Query("QUERY")
    //method name + method type

    @Query("DELETE FROM POI_Route")
    void deleteTable();

    @Insert
    void insertAll(List<POI_Route> poi_routes);
}