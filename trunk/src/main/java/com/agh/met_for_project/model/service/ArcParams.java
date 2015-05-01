package com.agh.met_for_project.model.service;


public class ArcParams {

    private String transitionName;
    private String placeName;
    private String arcType;
    private int value;

    public String getArcType() {
        return arcType;
    }

    public ArcParams setArcType(String arcType) {
        this.arcType = arcType;
        return this;
    }

    public String getPlaceName() {
        return placeName;
    }

    public ArcParams setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public ArcParams setTransitionName(String transitionName) {
        this.transitionName = transitionName;
        return this;
    }

    public int getValue() {
        return value;
    }

    public ArcParams setValue(int value) {
        this.value = value;
        return this;
    }
}
