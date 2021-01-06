package com.example.bbb.boundaryLayer.ui;

import androidx.lifecycle.ViewModel;

public class BBBViewmodel extends ViewModel {
    private static BBBViewmodel bbbViewmodel;
    private IPOIVistitedListener ipoiVistitedListener;

    public static BBBViewmodel getInstance(){
        if(bbbViewmodel == null){
            bbbViewmodel = new BBBViewmodel();
        }
        return bbbViewmodel;
    }

    public IPOIVistitedListener getIpoiVistitedListener() {
        return ipoiVistitedListener;
    }

    public void setIpoiVistitedListener(IPOIVistitedListener ipoiVistitedListener) {
        this.ipoiVistitedListener = ipoiVistitedListener;
    }
}
