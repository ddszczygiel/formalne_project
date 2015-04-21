package com.agh.met_for_project.model;


import java.util.*;

public class Transition {

    private List<OutArc> in;
    private List<InArc> out;
    private String name;
    private int priority;

    public Transition() {

        this.in = new ArrayList<>();
        this.out = new ArrayList<>();
    }

    public Transition(String name, int priority) {
        this();
        this.name = name;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<OutArc> getIn() {
        return in;
    }

    public List<InArc> getOut() {
        return out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExecutable() {

        for (OutArc outArc : in) {
            if ((outArc.getBegin().getState()-outArc.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    public void execute() {

        for (OutArc outArc : in) {
            outArc.getBegin().setState(outArc.getBegin().getState()-outArc.getValue());
        }
        for (InArc inArc : out) {
            inArc.getEnd().setState(inArc.getEnd().getState()+inArc.getValue());
        }
    }

    public boolean isExecutable(Map<String, Integer> actualPlacesStates) {

        for (OutArc outArc : in) {

            int actualPlaceState = actualPlacesStates.get(outArc.getBegin().getName());  //FIXME need to check ???
            if ((actualPlaceState-outArc.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    public Map<String, Integer> execute(Map<String, Integer> actualPlacesStates) {

        Map<String, Integer> newPlacesStates = new TreeMap<>();
        for (OutArc outArc : in) {

            String placeName = outArc.getBegin().getName();
            int actualPlaceState = actualPlacesStates.get(placeName);
            newPlacesStates.put(placeName, actualPlaceState-outArc.getValue());
        }
        for (InArc inArc : out) {

            String placeName = inArc.getEnd().getName();
            int actualPlaceState = actualPlacesStates.get(placeName);
            newPlacesStates.put(placeName, actualPlaceState+inArc.getValue());
        }

        return newPlacesStates;
    }

}
