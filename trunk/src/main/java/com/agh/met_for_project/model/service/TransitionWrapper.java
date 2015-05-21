package com.agh.met_for_project.model.service;



import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionWrapper {

    private Map<String, Integer> in;
    private Map<String, Integer> out;
    private String name;
    private int priority;

    public TransitionWrapper(Transition t) {

        name = t.getName();
        priority = t.getPriority();
        in = new HashMap<>();
        out = new HashMap<>();
        prepareConnections(t);
    }

    private void prepareConnections(Transition t) {

        for (Arc arc : t.getIn()) {
            in.put(arc.getPlaceName(), arc.getValue());
        }

        for (Arc arc : t.getOut()) {
            out.put(arc.getPlaceName(), arc.getValue());
        }
    }

    public Map<String, Integer> getIn() {
        return in;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getOut() {
        return out;
    }

    public int getPriority() {
        return priority;
    }
}
