package com.example.bbb.boundaryLayer.ui.spinner;

public class SpinnerItem {
    private final int image;
    private String name;

    public SpinnerItem(String name, int image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }
}
