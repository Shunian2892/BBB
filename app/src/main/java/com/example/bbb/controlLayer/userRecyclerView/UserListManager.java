package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;

import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.ArrayList;
import java.util.List;

public class UserListManager {
    private List<WalkedRoute> walkedRouteList;
    private DatabaseManager databaseManager;

    public UserListManager(Context context){
        walkedRouteList = new ArrayList<>();

        init(context);
    }

    private void init(Context context) {
        databaseManager = DatabaseManager.getInstance(context);
        databaseManager.initDatabase();
        UserReader();
    }

    private void UserReader() {
        walkedRouteList = databaseManager.getWalkedRoutes();
    }

    public List<WalkedRoute> getWalkedRouteList() {
        return walkedRouteList;
    }
}
