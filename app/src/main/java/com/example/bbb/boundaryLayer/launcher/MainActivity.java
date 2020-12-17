package com.example.bbb.boundaryLayer.launcher;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.MapFragment;
import com.example.bbb.boundaryLayer.ui.POIFragment;
import com.example.bbb.boundaryLayer.ui.POIListFragment;
import com.example.bbb.boundaryLayer.ui.ReplacePOI;
import com.example.bbb.boundaryLayer.ui.SettingsFragment;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ReplacePOI {
    private static final String BACK_STACK_TAG = "back_stack";
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private POIListFragment poiListFragment;
    FragmentManager fragmentManager;
    public BottomNavigationView bottomNav;

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    fragmentManager = getSupportFragmentManager();
                    switch (item.getItemId()) {
                        case R.id.menu_settings:
                            setSettingFragment(fragmentManager);
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).addToBackStack(null).commit();
                            break;
                        case R.id.menu_list:
                            setPoiListFragment(fragmentManager);
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, poiListFragment).addToBackStack(null).commit();
                            break;
                        case R.id.menu_map:
                            setMapFragment(fragmentManager);
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
                            break;
                    }
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1);
//        entry.
//        bottomNav.setSelectedItemId(item.getItemId());
//        bottomNav.getse
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
        databaseManager.initDatabase();
        databaseManager.testQueries();

        List<POI> poiList = databaseManager.getPOIs();
        List<Route> routeList = databaseManager.getRoutes();
        for (Route route : routeList) {
            databaseManager.addWalkedRoute(route.ID, new Date(System.currentTimeMillis()).toString());
        }

        List<WalkedRoute> testList = databaseManager.getWalkedRoutes();

        for (int i = 0; i < testList.size(); i++) {
            System.out.println("Walked route " + testList.get(i).routeID + " on " + testList.get(i).date);
        }

        System.out.println(databaseManager.searchLocation("Stadhouderspoort").ID);

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);

        setMapFragment(getSupportFragmentManager());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
    }

    public void setPoiListFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.fragment_poi_list) == null) {
            poiListFragment = new POIListFragment(this, this);
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
        poiListFragment.setButtonBackVisibility(false);
    }

    public void setSettingFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.fragment_settings) == null) {
            settingsFragment = new SettingsFragment();
        } else {
            settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.fragment_settings);
        }
    }

    public void setMapFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.map_fragment) == null) {
            mapFragment = new MapFragment(this, this);
        } else {
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }

    }

    @Override
    public void setDetailPOI(POI poi) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new POIFragment(poi, this)).addToBackStack(null).commit();
    }
}