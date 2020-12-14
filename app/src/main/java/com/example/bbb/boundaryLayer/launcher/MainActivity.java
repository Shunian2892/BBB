package com.example.bbb.boundaryLayer.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.HelpPopUp;
import com.example.bbb.boundaryLayer.ui.MapFragment;

public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                mapFragment.invalidateMapView();
            } catch (NullPointerException e) {
                // lazy handling of an improbable NPE
            }
        }
    };

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new HelpPopUp();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_fragment) == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.main_fragment, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.main_fragment);
        }

        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkReceiver);
        super.onDestroy();
    }
}