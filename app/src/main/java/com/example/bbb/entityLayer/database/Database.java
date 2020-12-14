package com.example.bbb.entityLayer.database;

import androidx.room.RoomDatabase;

import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.POI_Route;
import com.example.bbb.entityLayer.data.Route;

@androidx.room.Database(entities = {POI.class, Route.class, POI_Route.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract POIDao poiDao();
    public abstract RouteDao routeDao();
    public abstract PoiRouteDao poi_route_Dao();
}
