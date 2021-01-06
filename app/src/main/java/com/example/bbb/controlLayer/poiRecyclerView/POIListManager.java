package com.example.bbb.controlLayer.poiRecyclerView;

import android.content.Context;

import com.example.bbb.boundaryLayer.ui.UIViewModel;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;

import java.util.ArrayList;
import java.util.List;

public class POIListManager {
    private List<POI> POIList;
    private DatabaseManager databaseManager;
    private final UIViewModel viewModel;

    public POIListManager(UIViewModel viewModel) {
        POIList = new ArrayList<>();
        this.viewModel = viewModel;
        databaseManager = DatabaseManager.getInstance();
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
