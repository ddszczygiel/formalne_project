package com.agh.met_for_project.model.service;


public class PlaceActivityWrapper {

    private String placeName;
    private PlaceActivity placeActivity;

    public PlaceActivityWrapper(String placeName, PlaceActivity placeActivity) {
        this.placeName = placeName;
        this.placeActivity = placeActivity;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public PlaceActivity getPlaceActivity() {
        return placeActivity;
    }

    public void setPlaceActivity(PlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }
}
