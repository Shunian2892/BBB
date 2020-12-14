package com.example.bbb.controlLayer.poiRecyclerView;

import android.util.Log;

import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class POIListManager {
    private ArrayList<POI> POIList;

    public POIListManager(){
        POIList = new ArrayList<>();
        init();
    }

    private void init() {
       POIReader();
    }

    private void POIReader(){
        for(int i = 0; i<15; i++){
            POIList.add(new POI(Integer.toString(i),"x", "y", "beschrijving"));
        }

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

    public ArrayList<POI> getPOIList() {
        return POIList;
    }
}
