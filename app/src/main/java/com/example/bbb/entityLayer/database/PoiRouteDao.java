package com.example.bbb.entityLayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bbb.entityLayer.data.POIRoute;

import java.util.List;

@Dao
public interface PoiRouteDao {

    @Query("SELECT * FROM POI_ROUTE")
    List<POIRoute> getAll();

    //add new queries here
    //@Query("QUERY")
    //method name + method type

    @Query("DELETE FROM POI_ROUTE")
    void deleteTable();

    @Insert
    void insertAll(List<POIRoute> poi_routes);
}
