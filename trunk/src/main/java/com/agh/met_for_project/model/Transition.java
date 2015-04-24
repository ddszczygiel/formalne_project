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

        for (Arc arc : in) {
            if ((arc.getPlaceState()-arc.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    public void execute() {

        for (Arc arc : in) {
            arc.setPlaceState(arc.getPlaceState() - arc.getValue());
        }
        for (Arc arc : out) {
            arc.setPlaceState(arc.getPlaceState() + arc.getValue());
        }

    }

    public boolean isExecutable(Map<String, Integer> actualPlacesStates) {

        for (Arc arc : in) {

            int actualPlaceState = actualPlacesStates.get(arc.getPlaceName());  //FIXME need to check ???
            if ((actualPlaceState-arc.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    public Map<String, Integer> execute(Map<String, Integer> actualPlacesStates) {

        Map<String, Integer> newPlacesStates = new TreeMap<>(actualPlacesStates);
        for (Arc arc : in) {

            String placeName = arc.getPlaceName();
            int actualPlaceState = newPlacesStates.get(placeName);
            newPlacesStates.put(placeName, actualPlaceState-arc.getValue());
        }
        for (Arc arc : out) {

            String placeName = arc.getPlaceName();
            int actualPlaceState = newPlacesStates.get(placeName);
            newPlacesStates.put(placeName, actualPlaceState+arc.getValue());
        }

        return newPlacesStates;
    }

}
