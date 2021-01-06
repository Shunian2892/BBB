package com.example.bbb.boundaryLayer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;

import java.util.ArrayList;
import java.util.List;

public class UIViewModel extends ViewModel {

    private MutableLiveData<List<POI>> pointOfInterests;
    private MutableLiveData<POI> selectedPOI;
    private MutableLiveData<Boolean> backButtonState;
    private MutableLiveData<Integer> currentFragment;
    private MutableLiveData<Integer> selectedRoute;
    private IMapChanged iMapChanged;
    private MutableLiveData<Route> routePopUpSelectedRoute;
    private POIClickListener poiClickListener;
    private MutableLiveData<POI> visiblePOI;

    public void init(int currentFragment) {
        if (pointOfInterests != null) {
            return;
        }

        pointOfInterests = new MutableLiveData<>();
        selectedPOI = new MutableLiveData<>();
        backButtonState = new MutableLiveData<>(false);
        this.currentFragment = new MutableLiveData<>(currentFragment);
        selectedRoute = new MutableLiveData<>(0);
        routePopUpSelectedRoute = new MutableLiveData<>();
        visiblePOI = new MutableLiveData<>();
    }

    public LiveData<Boolean> getBackButtonState(){return backButtonState;}

    public LiveData<POI> getSelectedPOI() {
        return selectedPOI;
    }

    public void setSelectedPOI(POI poi) {
        selectedPOI.setValue(poi);
    }

    public LiveData<List<POI>> getPOIs() {
        return pointOfInterests;
    }

    public void setPointOfInterests(List<POI> poiList) {
        pointOfInterests.setValue(poiList);
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

    public void setIMapChanged(IMapChanged iMapChanged) {
        this.iMapChanged = iMapChanged;
    }

    public IMapChanged getIMapChanged(){
        return iMapChanged;
    }

    public LiveData<Route> getRoutePopUpSelectedRoute() {
        return routePopUpSelectedRoute;
    }

    public void setRoutePopUpSelectedRoute(Route routePopUpSelectedRoute) {
        this.routePopUpSelectedRoute.setValue(routePopUpSelectedRoute);
    }

    public POIClickListener getPoiClickListener() {
        return poiClickListener;
    }

    public void setPoiClickListener(POIClickListener poiClickListener) {
        this.poiClickListener = poiClickListener;
    }

    public LiveData<POI> getVisiblePOI() {
        return visiblePOI;
    }

    public void setVisiblePOI(POI poi){
        visiblePOI.setValue(poi);
    }
}
