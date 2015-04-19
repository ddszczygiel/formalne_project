package com.agh.met_for_project.model;


import java.util.ArrayList;
import java.util.List;

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

    public void setIn(List<OutArc> in) {
        this.in = in;
    }

    public List<InArc> getOut() {
        return out;
    }

    public void setOut(List<InArc> out) {
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
