package com.agh.met_for_project.model.service;


import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.agh.met_for_project.network.PetriesNetwork;

import java.util.ArrayList;
import java.util.List;

public class NetworkWrapper {

    private List<Place> places;
    private List<TransitionWrapper> transitions;

    public NetworkWrapper(PetriesNetwork network) {

        places = network.getPlaces();
        transitions = new ArrayList<>();
        prepareTransitions(network.getTransitions());

    }

    private void prepareTransitions(List<Transition> tlist) {

        for (Transition t : tlist) {
            transitions.add(new TransitionWrapper(t));
        }
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<TransitionWrapper> getTransitions() {
        return transitions;
    }
}
