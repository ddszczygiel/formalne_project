package com.agh.met_for_project.network;


import com.agh.met_for_project.model.NetworkState;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ReachTree {

    private final int MAX_DEPTH = 30;

    @Autowired
    private PetriesNetwork petriesNetwork;

    private NetworkState initialNode;
    private Map<String, NetworkState> states;
    private Queue<NetworkState> operationQueue;

    @PostConstruct
    public void setUp() {

        operationQueue = new LinkedList<>();
        states = new HashMap<>();
    }

    public void setInitialNode() {

        initialNode = new NetworkState();
        for (Place p : petriesNetwork.getPlaces()) {
            initialNode.getStates().put(p.getName(), p.getState());
        }
        String statesString = initialNode.getStatesString();
        initialNode.getPath().add(statesString);
        states.put(statesString, initialNode);
    }

    public void buildReachTree() {

        setUp();
        setInitialNode();
        operationQueue.add(initialNode);

        while (!operationQueue.isEmpty()) {

            NetworkState state = operationQueue.poll();     // can not be null because of while condition
            boolean noActiveTransitions = true;

            for (Transition t : petriesNetwork.getTransitions()) {

                if (t.isExecutable(state.getStates())) {

                    noActiveTransitions = false;
                    NetworkState childState = new NetworkState(state);
                    Map<String, Integer> newState = t.execute(state.getStates());
                    childState.getStates().putAll(newState);
                    childState.setExecutedTransitionName(t.getName());
                    state.getNodes().add(childState);
                    String newStateString = childState.getStatesString();

                    if (states.containsKey(newStateString)) {

                        childState.setDuplicate(true);
                    } else {

                        states.put(newStateString, childState);
                        childState.getPath().add(newStateString);

                        if (state.getPath().size() < MAX_DEPTH) {
                            operationQueue.add(childState);
                        }
                    }
                }
            }

            if (noActiveTransitions) {
                state.setDead(true);
            }
        }
    }

    public void displayTree() {

        Queue<NetworkState> states = new LinkedList<>();
        states.add(initialNode);

        while (!states.isEmpty()) {

            NetworkState current = states.poll();
            for (NetworkState child : current.getNodes()) {
                states.add(child);
            }
            System.out.println(current);
        }
    }

}
