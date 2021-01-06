package com.example.bbb.controlLayer.poiRecyclerView;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.boundaryLayer.ui.UIViewModel;
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
    private UIViewModel viewModel;

    public POIListManager(Context context, UIViewModel viewModel) {
        POIList = new ArrayList<>();
        this.appContext = context;
        this.viewModel = viewModel;
        init();
    }

    private void init() {
        databaseManager = DatabaseManager.getInstance(appContext);
        POIReader();
    }

    public void POIReader() {
        if (viewModel.getPOIs().getValue() == null) {
            this.POIList = databaseManager.getPOIs();
        } else if (viewModel.getPOIs().getValue().size() == 0) {
            this.POIList = databaseManager.getPOIs();
        } else {
            this.POIList = viewModel.getPOIs().getValue();
        }

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

    public void setPOIList(String searchString) {
        this.POIList.clear();
        if (!searchString.equals("")) {
            if (databaseManager.searchLocation(searchString).size() != 0) {
                this.POIList = databaseManager.searchLocation(searchString);
            }
        } else {
            this.POIList = databaseManager.getPOIs();
        }
    }
}
