package com.agh.met_for_project.model;



public class OutArc implements Arc {

    private int value;
    private Place begin;

    public int getValue() {
        return value;
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
