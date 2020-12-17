package com.example.bbb.boundaryLayer.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.Button;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.LocaleHelper;
import com.example.bbb.boundaryLayer.ui.MapFragment;
import com.example.bbb.boundaryLayer.ui.POIFragment;
import com.example.bbb.boundaryLayer.ui.POIListFragment;
import com.example.bbb.boundaryLayer.ui.ReplacePOI;
import com.example.bbb.boundaryLayer.ui.RoutePopUp;
import com.example.bbb.boundaryLayer.ui.SettingsFragment;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ReplacePOI {
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private POIListFragment poiListFragment;



    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentManager fm = getSupportFragmentManager();
                    switch (item.getItemId()) {
                        case R.id.menu_settings:
                            setSettingFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
                            break;
                        case R.id.menu_list:
                            setPoiListFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container, poiListFragment).commit();
                            break;
                        case R.id.menu_map:
                            setMapFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
                            break;
                    }

                    return true;
                }
            };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the locale has changed
        if (!LocaleHelper.getLanguage(this).equals(newConfig.locale)) {
            LocaleHelper.setLocale(this, newConfig.locale.getLanguage());

            this.setContentView(R.layout.activity_main);
            BottomNavigationView navigationView = (BottomNavigationView)  this.findViewById(R.id.bottomNavigationView);
            navigationView.setSelectedItemId(R.id.menu_settings);
            navigationView.setOnNavigationItemSelectedListener(navlistener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
        String currentLang = prefs.getString("language", "No name defined");//"No name defined" is the default value.
        System.out.println("########" + currentLang);

        LocaleHelper.setLocale(this, currentLang);

        setContentView(R.layout.activity_main);


        DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
        databaseManager.initDatabase();
        databaseManager.testQueries();

        List<POI> poiList = databaseManager.getPOIs();
        List<Route> routeList = databaseManager.getRoutes();

        databaseManager.addWalkedRoute(1, new Date(System.currentTimeMillis()).toString());
        List<WalkedRoute> testList = databaseManager.getWalkedRoutes();

        for (int i = 0; i < testList.size(); i++) {
            System.out.println("Walked route " + testList.get(i).routeID + " on " + testList.get(i).date);
        }

        System.out.println(databaseManager.searchLocation("Stadhouderspoort").ID);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);
    }

    public void setPoiListFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.fragment_poi_list) == null){
            poiListFragment = new POIListFragment(this,this);
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
    }
    public void setSettingFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.fragment_settings)== null){
            settingsFragment = new SettingsFragment();
        } else {
            settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.fragment_settings);
        }
    }
    public void setMapFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
        } else {
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }
    } @Override
    public void setDetailPOI(POI poi) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new POIFragment(poi, this)).commit();
    }


}