package com.agh.met_for_project.model;



public class InArc implements Arc {

    private int value;
    private Transition begin;
    private Place end;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Transition getBegin() {
        return begin;
    }

    public void setBegin(Transition begin) {
        this.begin = begin;
    }

    public Place getEnd() {
        return end;
    }

    public void setEnd(Place end) {
        this.end = end;
    }
}
