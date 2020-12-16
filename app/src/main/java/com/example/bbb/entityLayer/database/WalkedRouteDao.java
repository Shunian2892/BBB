package com.example.bbb.entityLayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.List;


@Dao
public interface WalkedRouteDao {

    @Query("SELECT * FROM WALKED_ROUTE")
    List<WalkedRoute> getAll();

    @Query("DELETE FROM WALKED_ROUTE")
    void deleteTable();

    @Insert
    void insertAll(List<WalkedRoute> routes);

    @Insert
    void insert(WalkedRoute walkedRoute);


}
