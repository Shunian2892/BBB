package com.example.bbb.entityLayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.POI_Route;
import com.example.bbb.entityLayer.data.Route;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM Route")
    List<Route> getAll();

    @Insert
    void insert (Route route);

    @Query("DELETE FROM Route")
    void deleteTable();

    @Query("SELECT * FROM POI_Route, POI WHERE POI_Route.RouteID = :routeID AND POI.ID = POI_Route.PoiID")
    List<POI> getPOIs(int routeID);
}
