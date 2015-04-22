package com.agh.met_for_project.model;



public class OutArc implements Arc {

    private int value;
    private Place begin;

    public int getValue() {
        return value;
    }

    @Override
    public int getPlaceState() {
        return begin.getState();
    }

    @Override
    public void setPlaceState(int newState) {
        begin.setState(newState);
    }

    @Override
    public String getPlaceName() {
        return begin.getName();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Place getBegin() {
        return begin;
    }

    public void setBegin(Place begin) {
        this.begin = begin;
    }

}
