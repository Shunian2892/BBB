package com.example.bbb.entityLayer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bbb.entityLayer.data.POI;

import java.util.List;

@Dao
public interface POIDao {

    @Query("SELECT * FROM POI")
    List<POI> getAll();

    @Query("SELECT * FROM POI WHERE POIName LIKE '%' || :name || '%'")
    List<POI> matchedPOIs(String name);

    //add new queries here
    //@Query("QUERY")
    //method name + method type
    @Query("UPDATE POI SET IsVisited = :visited WHERE POIName LIKE :poiName")
    void changePOIState(String poiName, boolean visited);

    @Query("DELETE FROM POI")
    void deleteTable();

    @Insert
    void insert (POI poi);

    @Insert
    void insertAll(List<POI> pois);
}
