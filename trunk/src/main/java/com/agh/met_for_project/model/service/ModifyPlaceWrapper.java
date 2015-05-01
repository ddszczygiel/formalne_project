package com.agh.met_for_project.model.service;


public class ModifyPlaceWrapper {

    private String actualName;
    private String newName;
    private int newState;

    public int getNewState() {
        return newState;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }
}
