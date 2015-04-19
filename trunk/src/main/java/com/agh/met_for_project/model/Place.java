package com.agh.met_for_project.model;



public class Place {

    private int state;
    private String name;

    public Place(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public Place() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
