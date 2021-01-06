package com.example.bbb.entityLayer.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.util.TableInfo;

@Entity
public class Route {

    @PrimaryKey
    public int ID;
    public String RouteName_nl;
    public String RouteName_en;
    public String RouteName_fr;

}
