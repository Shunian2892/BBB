package com.example.bbb.boundaryLayer.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.RoutePopUp;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new RoutePopUp();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });

        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
        databaseManager.initDatabase();
        databaseManager.testQueries();

        List<POI> poiList = databaseManager.getPOIs();
        List<Route> routeList = databaseManager.getRoutes();

        databaseManager.addWalkedRoute(1 , new Date(System.currentTimeMillis()).toString());
        List<WalkedRoute> testList = databaseManager.getWalkedRoutes();

        for (int i = 0; i < testList.size(); i++) {
            System.out.println(testList.get(i).routeID);
            System.out.println(testList.get(i).date);
        }

        System.out.println(databaseManager.searchLocation("Stadhouderspoort").ID);


    }





}