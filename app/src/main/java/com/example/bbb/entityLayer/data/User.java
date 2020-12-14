package com.example.bbb.entityLayer.data;

import java.util.List;

public class User {
private double distanceWalked;
private List<Route> routesCompleted;

    public User(double distanceWalked, List<Route> routesCompleted) {
        this.distanceWalked = distanceWalked;
        this.routesCompleted = routesCompleted;
    }
}
