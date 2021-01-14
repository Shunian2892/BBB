package com.example.bbb.entityLayer.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "POI_ROUTE",
        primaryKeys = {"RouteID", "PoiID"},
        foreignKeys = {
            @ForeignKey(entity = POI.class,
                        parentColumns = "ID",
                        childColumns = "PoiID"),
            @ForeignKey(entity = Route.class,
                        parentColumns = "ID",
                        childColumns = "RouteID")
        })
public class POIRoute {

    public int RouteID;
    public int PoiID;
}
