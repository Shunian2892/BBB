package com.example.bbb.boundaryLayer.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.HelpPopUp;
import com.example.bbb.boundaryLayer.ui.MapFragment;
import com.example.bbb.boundaryLayer.ui.POIFragment;
import com.example.bbb.boundaryLayer.ui.POIListFragment;
import com.example.bbb.boundaryLayer.ui.ReplacePOI;
import com.example.bbb.boundaryLayer.ui.SettingsFragment;
import com.example.bbb.controlLayer.poiRecyclerView.POIListManager;
import com.example.bbb.entityLayer.data.POI;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ReplacePOI {
    private POIListFragment poiListFragment;
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new HelpPopUp();
            Toast.makeText(getApplicationContext(),"pressed button", Toast.LENGTH_LONG).show();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentManager fm = getSupportFragmentManager();
                    switch (item.getItemId()) {
                        case R.id.menu_settings:
                            setSettingFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container,settingsFragment).commit();
                            break;
                        case R.id.menu_list:
                            setPoiListFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container,poiListFragment).commit();
                            break;
                        case R.id.menu_map:
                            setMapFragment(fm);
                            fm.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
                            break;
                    }

                    return true;
                }
            };

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_navigation_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                return true;
            case R.id.menu_list:
                Toast.makeText(getApplicationContext(),"pressed list", Toast.LENGTH_LONG).show();
                Log.d("main", "pressed list");
                setPoiListFragment();
                return true;
            case R.id.menu_map:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }*/
    public void setPoiListFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.fragment_poi_list) == null){
            poiListFragment = new POIListFragment(this,this);
        }else{
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
    }
    public void setSettingFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.fragment_settings)== null){
            settingsFragment = new SettingsFragment();
        }else{
            settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.fragment_settings);
        }
    }
    public void setMapFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
        }else{
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }
    }


    @Override
    public void setDetailPOI(POI poi) {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new POIFragment(poi)).commit();
    }
}