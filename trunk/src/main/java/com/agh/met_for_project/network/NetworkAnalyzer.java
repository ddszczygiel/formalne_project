package com.agh.met_for_project.network;

import com.agh.met_for_project.model.NetworkState;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by admin on 2015-04-22.
 */
@Component
public class NetworkAnalyzer {

    private final int MAX_DEPTH = 10;

    @Autowired
    private PetriesNetwork petriesNetwork;

    private Set<String> networkStates;
    private NetworkState initialNode;
    private List<Transition> networkTransitions;


    @PostConstruct
    public void setUp() {

        networkStates = new HashSet<>();
        networkTransitions = petriesNetwork.getTransitions();
    }

    public void setInitialNode() {

        initialNode = new NetworkState();
        for (Place p : petriesNetwork.getPlaces()) {
            initialNode.getStates().put(p.getName(), p.getState());
        }
        networkStates.add(initialNode.getStatesString());
    }

    public void generate() {

        setUp();
        setInitialNode();
        executeTransitions(initialNode, 1);
    }

    // current depth could be as a method parameter ??
    public void executeTransitions(NetworkState networkState, int currentDepth) {

        if (currentDepth > MAX_DEPTH) {
            return;
        }

        boolean noActiveTransitions = true;
        for (Transition t : networkTransitions) {

            if (t.isExecutable(networkState.getStates())) {

                noActiveTransitions = false;
                NetworkState childNetworkState = new NetworkState();
                networkState.getNodes().add(childNetworkState);
                Map<String, Integer> newState = t.execute(networkState.getStates());
                childNetworkState.getStates().putAll(newState);
                childNetworkState.setExecutedTransitionName(t.getName());
                String newStatesString = childNetworkState.getStatesString();
                if (networkStates.contains(newStatesString)) {
                    childNetworkState.setDuplicate(true);
                } else {

                    networkStates.add(newStatesString);
                    executeTransitions(childNetworkState, currentDepth+1);
                }
            }
        }

        if (noActiveTransitions) {
            networkState.setDead(true);
        }
    }

    public void printTree() {

        displayNode(initialNode);
    }

    private void displayNode(NetworkState networkState) {

        System.out.println(networkState);
        for (NetworkState state : networkState.getNodes()) {
            displayNode(state);
        }
    }

}
