package com.agh.met_for_project.model;



public interface Arc {
    // FIXME arc are made in context of PLACE ( InArc - pointed to Place; OutArc - pointed to Transition )
    int getValue();
    void setValue(int value);
    int getPlaceState();
    void setPlaceState(int newState);
    String getPlaceName();
}
