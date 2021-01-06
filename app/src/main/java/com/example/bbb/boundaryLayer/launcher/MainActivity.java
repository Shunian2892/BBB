package com.example.bbb.boundaryLayer.launcher;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.LocaleHelper;
import com.example.bbb.boundaryLayer.ui.MapFragment;
import com.example.bbb.boundaryLayer.ui.POIListFragment;
import com.example.bbb.boundaryLayer.ui.SettingsFragment;
import com.example.bbb.boundaryLayer.ui.UIViewModel;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.geofencing.GeoFenceSetup;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String BACK_STACK_TAG = "back_stack";
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private POIListFragment poiListFragment;
    FragmentManager fragmentManager;
    private BottomNavigationView bottomNav;
    private UIViewModel viewModel;
    private int currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    currentFragment = item.getItemId();
                    viewModel.setCurrentFragment(currentFragment);
                    item.setChecked(true);
                    setDisplayFragment(currentFragment);

                    return true;
                }
            };

    private void setDisplayFragment(int item) {
        fragmentManager = getSupportFragmentManager();
        switch (item) {
            case R.id.menu_settings:
                setSettingFragment(fragmentManager);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).addToBackStack(null).commit();
                break;
            case R.id.menu_list:
                setPoiListFragment(fragmentManager);
                viewModel.setPointOfInterests(DatabaseManager.getInstance(getApplicationContext()).getPOIs());
                fragmentManager.beginTransaction().replace(R.id.fragment_container, poiListFragment).addToBackStack(null).commit();
                break;
            case R.id.menu_map:
                setMapFragment(fragmentManager);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UIViewModel.class);
        viewModel.init(R.id.menu_map);

        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
        String currentLang = prefs.getString("language", Locale.getDefault().getLanguage());//"No name defined" is the default value.
        System.out.println("########" + currentLang);

        //Set the theme of the app based on colorblind mode
        SharedPreferences colorBlindPrefs = getSharedPreferences("colorblind", MODE_PRIVATE);
        boolean currentColorMode = colorBlindPrefs.getBoolean("colorblind", false);//"No name defined" is the default value.

        if(currentColorMode){
            setTheme(R.style.Theme_BBB_colorblind);
        } else if (!currentColorMode){
            setTheme(R.style.Theme_BBB);
        }

        LocaleHelper.setLocale(this, currentLang);

        setContentView(R.layout.activity_main);

        DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
        databaseManager.initDatabase();
        databaseManager.testQueries();

        List<Route> routeList = databaseManager.getRoutes();
        for (Route route : routeList) {
            databaseManager.addWalkedRoute(route.ID, new Date(System.currentTimeMillis()).toString());
        }

        //Setup Geofencing
        //GeoFenceSetup geoFenceSetup = new GeoFenceSetup(getApplicationContext(), this, poiList);
        //geoFenceSetup.setupGeoFencing(poiList);

        List<WalkedRoute> testList = databaseManager.getWalkedRoutes();

        for (int i = 0; i < testList.size(); i++) {
            System.out.println("Walked route " + testList.get(i).routeID + " on " + testList.get(i).date);
        }

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);

        setDisplayFragment(viewModel.getCurrentFragment().getValue());

    }

    public void setPoiListFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.fragment_poi_list) == null) {
            poiListFragment = new POIListFragment();
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
        viewModel.setBackButtonState(false);
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
            mapFragment = new MapFragment();
        } else {
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }
    }

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
}