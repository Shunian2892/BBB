package com.example.bbb.boundaryLayer.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.HelpPopUp;
import com.example.bbb.boundaryLayer.ui.MapFragment;
import com.example.bbb.controlLayer.gps.OpenRouteService;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OpenRouteService openRouteService = new OpenRouteService();

        openRouteService.getRoute(new GeoPoint(8.681496,
                49.41461), new GeoPoint(8.687872,
                49.420318), "driving-car");
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(view -> {
//            DialogFragment dialogFragment = new HelpPopUp();
//            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
//        });

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_fragment) == null) {
            mapFragment = new MapFragment();
            fragmentManager.beginTransaction().add(R.id.main_fragment, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.main_fragment);
        }
    }
}