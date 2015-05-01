package com.agh.met_for_project.model.service;


public class ModifyTransitionWrapper {

    private String actualName;
    private String newName;
    private int newPriority;

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

    public int getNewPriority() {
        return newPriority;
    }

    public void setNewPriority(int newPriority) {
        this.newPriority = newPriority;
    }
}
