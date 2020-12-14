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

    @Query("SELECT * FROM POI WHERE POIName LIKE :searchString")
    List<POI> matchedPOIs(String searchString);

    @Insert
    void insertAll(List<POI> pois);

    @Insert
    void insert (POI poi);

    @Query("DELETE FROM POI")
    public void deleteTable();
}
