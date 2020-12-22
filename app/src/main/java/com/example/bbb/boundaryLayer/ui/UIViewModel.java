package com.example.bbb.boundaryLayer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bbb.entityLayer.data.POI;

import java.util.List;

public class UIViewModel extends ViewModel {

    private MutableLiveData<List<POI>> pointOfInterests;
    private MutableLiveData<POI> selectedPOI;
    private MutableLiveData<Boolean> backButtonState;
    private MutableLiveData<Integer> currentFragment;
    private MutableLiveData<Integer> selectedRoute;

    public void init(int currentFragment) {
        if (pointOfInterests != null) {
            return;
        }

        pointOfInterests = new MutableLiveData<>();
        selectedPOI = new MutableLiveData<>();
        backButtonState = new MutableLiveData<>(false);
        this.currentFragment = new MutableLiveData<>(currentFragment);
        selectedRoute = new MutableLiveData<>(0);
    }

    public LiveData<Boolean> getBackButtonState(){return backButtonState;}

    public LiveData<POI> getSelectedPOI() {
        return selectedPOI;
    }

    public LiveData<List<POI>> getPOIs() {
        return pointOfInterests;
    }

    public void setPointOfInterests(List<POI> poiList) {
        pointOfInterests.setValue(poiList);
    }

    public void setSelectedPOI(POI poi) {
        selectedPOI.setValue(poi);
    }

    public void setBackButtonState(boolean state){
        backButtonState.setValue(state);
    }

    public LiveData<Integer> getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(int currentFragment) {
        this.currentFragment.setValue(currentFragment);
    }

    public LiveData<Integer> getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(int pos) {
        this.selectedRoute.setValue(pos);
    }
}
