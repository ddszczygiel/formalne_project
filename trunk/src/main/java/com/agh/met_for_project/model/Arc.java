package com.agh.met_for_project.model;



public interface Arc {
    // FIXME arc are made in context of PLACE ( InArc - pointed to Place; OutArc - pointed to Transition )
    public int getValue();
    public int getPlaceState();
    public void setPlaceState(int newState);
    public String getPlaceName();
}
