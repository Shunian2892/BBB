package com.example.bbb.controlLayer.poiRecyclerView;

import android.content.Context;
import android.util.Log;

import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class POIListManager {
    private List<POI> POIList;
    private Context appContext;
    private DatabaseManager databaseManager;

    public POIListManager(Context context){
        POIList = new ArrayList<>();
        this.appContext = context;
        init();
    }

    private void init() {
        databaseManager = DatabaseManager.getInstance(appContext);
        databaseManager.initDatabase();
        POIReader();
    }

    private void POIReader(){
        POIList = databaseManager.getPOIs();

//        for(int i = 0; i<15; i++){
//            POIList.add(new POI(Integer.toString(i),"x", "y", "beschrijving"));
//        }

       /* try {
            File poiFile = new File();
            Scanner reader = new Scanner(poiFile);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String[] POIItem = line.split("\t");
                POI poi = new POI(POIItem[2],POIItem[0],POIItem[1],POIItem[3]);
                Log.d("ListManager", poi.toString());
                this.POIList.add(poi);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public List<POI> getPOIList() {
        return POIList;
    }
}
