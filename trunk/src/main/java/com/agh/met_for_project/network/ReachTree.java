package com.agh.met_for_project.network;


import com.agh.met_for_project.model.NetworkState;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ReachTree {

    @Autowired
    private PetriesNetwork petriesNetwork;

    private NetworkState initialNode;
    private Map<String, NetworkState> states;
    private List<Transition> networkTransitions;
    private Queue<NetworkState> operationQueue;

    @PostConstruct
    public void setUp() {

        operationQueue = new LinkedList<>();
        states = new HashMap<>();
        networkTransitions = petriesNetwork.getTransitions();
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

            for (Transition t : networkTransitions) {

                if (t.isExecutable(state.getStates())) {

                    noActiveTransitions = false;
                    NetworkState childState = new NetworkState(state);
                    Map<String, Integer> newState = t.execute(state.getStates());
                    childState.getStates().putAll(newState);
                    childState.setExecutedTransitionName(t.getName());
                    String newStateString = childState.getStatesString();

                    if (states.containsKey(newStateString)) {

                        childState.setDuplicate(true);  // useless with next line :D
                        state.getNodes().add(states.get(newStateString));  //adding reference to duplicated NetworkState - not the new one !!
                    } else {

                        state.getNodes().add(childState);   // adding reference to new state
                        states.put(newStateString, childState);
                        operationQueue.add(childState);
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
