package com.agh.met_for_project.model.service;



public class PossibleTransitionWrapper {

    private String name;
    private int priority;

    public PossibleTransitionWrapper(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
