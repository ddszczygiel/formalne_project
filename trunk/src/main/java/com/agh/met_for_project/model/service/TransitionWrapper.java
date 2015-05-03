package com.agh.met_for_project.model.service;



import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionWrapper {

    private List<String> in;
    private List<String> out;
    private String name;
    private int priority;

    public TransitionWrapper(Transition t) {

        name = t.getName();
        priority = t.getPriority();
        in = new ArrayList<>();
        out = new ArrayList<>();
        prepareConnections(t);
    }

    private void prepareConnections(Transition t) {

        for (Arc arc : t.getIn()) {
            in.add(arc.getPlaceName());
        }

        for (Arc arc : t.getOut()) {
            out.add(arc.getPlaceName());
        }
    }

    public List<String> getIn() {
        return in;
    }

    public String getName() {
        return name;
    }

    public List<String> getOut() {
        return out;
    }

    public int getPriority() {
        return priority;
    }
}
