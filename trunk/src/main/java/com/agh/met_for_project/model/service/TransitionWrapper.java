package com.agh.met_for_project.model.service;



import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.Pair;
import com.agh.met_for_project.model.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionWrapper {

    private List<Pair> in;
    private List<Pair> out;
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
            in.add(new Pair(arc.getPlaceName(), arc.getValue()));
        }

        for (Arc arc : t.getOut()) {
            out.add(new Pair(arc.getPlaceName(), arc.getValue()));
        }
    }

    public List<Pair> getIn() {
        return in;
    }

    public String getName() {
        return name;
    }

    public List<Pair> getOut() {
        return out;
    }

    public int getPriority() {
        return priority;
    }
}
