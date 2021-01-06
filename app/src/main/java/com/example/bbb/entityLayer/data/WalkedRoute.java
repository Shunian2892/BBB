package com.example.bbb.entityLayer.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "WALKED_ROUTE",
        primaryKeys = {"routeID", "date"},
        foreignKeys = {
                @ForeignKey(entity = Route.class,
                        parentColumns = "ID",
                        childColumns = "routeID"),
        })

public class WalkedRoute {
    @NonNull
    public int routeID;
    @NonNull
    public String date;
    public Double distance;
}
