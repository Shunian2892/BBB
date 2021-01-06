package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;

import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.List;

public class UserListManager {
    private final List<WalkedRoute> walkedRouteList;
    private final DatabaseManager databaseManager;
    private final String currentLanguage;

    public UserListManager(String currentLanguage){
        databaseManager = DatabaseManager.getInstance();
        walkedRouteList = databaseManager.getWalkedRoutes();
        this.currentLanguage = currentLanguage;
    }

    public String getRouteLanguage(int routeID){
        String routeName = "";
        switch (currentLanguage) {
            case "en":
                routeName = databaseManager.getRoute(routeID).RouteName_en;
                break;
            case "fr":
                routeName = databaseManager.getRoute(routeID).RouteName_fr;
                break;
            case "nl":
                routeName = databaseManager.getRoute(routeID).RouteName_nl;
                break;
        }
        return routeName;
    }

    public List<POI> getPOIAmount(WalkedRoute walkedRoute){
        return databaseManager.getPOIsFromRoute(walkedRoute.routeID);
    }

    public List<WalkedRoute> getWalkedRouteList() {
        return walkedRouteList;
    }
}
