package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;

import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.ArrayList;
import java.util.List;

public class UserListManager {
    private List<WalkedRoute> walkedRouteList;
    private Context appContext;
    private DatabaseManager databaseManager;

    public UserListManager(Context context){
        walkedRouteList = new ArrayList<>();
        this.appContext = context;
        init();
    }

    private void init() {
        databaseManager = DatabaseManager.getInstance(appContext);
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
