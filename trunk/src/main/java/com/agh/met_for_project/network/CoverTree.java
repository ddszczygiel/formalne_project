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
public class CoverTree {

    @Autowired
    private PetriesNetwork petriesNetwork;

    private NetworkState initialNode;
    private Set<String> states;
    private List<NetworkState> allStates;
    private Queue<NetworkState> operationQueue;

    @PostConstruct
    public void setUp() {

        operationQueue = new LinkedList<>();
        states = new HashSet<>();
        allStates = new ArrayList<>();
    }

    public List<NetworkState> getStates() {

        return allStates;
    }

    public void setInitialNode() {

        initialNode = new NetworkState();
        for (Place p : petriesNetwork.getPlaces()) {
            initialNode.getStates().put(p.getName(), p.getState());
        }
        String statesString = initialNode.getState();
        initialNode.getPath().add(statesString);
        states.add(statesString);
        allStates.add(initialNode);
    }

    public void buildCoverTree() {

        setUp();
        setInitialNode();
        operationQueue.add(initialNode);

        while (!operationQueue.isEmpty()) {

            NetworkState state = operationQueue.poll();     // can not be null because of while condition
            boolean noActiveTransitions = true;

            List<Transition> executableTransitions = petriesNetwork.possibleTransitions(state.getStates());
            if (!executableTransitions.isEmpty()) {

                noActiveTransitions = false;
                if (petriesNetwork.isPrioritySimulation()) {

                    Transition rand = executableTransitions.get(new Random().nextInt(executableTransitions.size()));
                    executeTransition(state, rand);
                } else {

                    for (Transition t : executableTransitions) {
                        executeTransition(state, t);
                    }
                }
            }

            if (noActiveTransitions) {
                state.setDead(true);
            }
        }

        petriesNetwork.getStatus().setCoverTreeStatus(Status.TreeStatus.ACTUAL);
    }

    public void executeTransition(NetworkState state, Transition t) {

        NetworkState childState = new NetworkState(state);
        state.getNodes().add(childState);
        Map<String, Integer> newState = t.execute(state.getStates());
        childState.getStates().putAll(newState);
        childState.setExecutedTransitionName(t.getName());
        childState.getExecutedTransitions().add(t.getName());   // FIXME new
        String newStatesString = childState.getState();
        allStates.add(childState);
        if (states.contains(newStatesString)) {
            childState.setDuplicate(true);
            childState.getPath().add(newStatesString);
        } else {

            String coverState = getCoverState(childState.getStatesValues(), childState.getPath());
            if (coverState == null) {
                childState.getPath().add(newStatesString);
                states.add(newStatesString);
            } else {

                childState.getPath().add(coverState);
                updateMapValues(coverState, childState.getStates());
                states.add(coverState);
            }

            if (state.getPath().size() < petriesNetwork.getMaxDepth()) {
                operationQueue.add(childState);
            }
        }
    }

    private void updateMapValues(String coverString, Map<String, Integer> actualStates) {

        int i = 0;
        String[] values = coverString.split(NetworkState.SEPARATOR);
        for (Map.Entry<String, Integer> entry : actualStates.entrySet()) {

            entry.setValue(Integer.parseInt(values[i++]));
        }
    }

    private int[] unpackStateString(String state) {

        String[] values = state.split(NetworkState.SEPARATOR);
        int[] tab = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            tab[i] = Integer.parseInt(values[i]);
        }

        return tab;
    }

    // FIXME check condition for comparing states with infinity = -1
    private String coverString(int[] cover, int[] old) {

        int[] tab = cover.clone();
        int i;
        for (i = 0; i < tab.length; i++) {

            if (tab[i] < 0) {
                continue;
            }
            if ((tab[i] < old[i]) || (old[i] < 0)) {
                break;
            } else if (tab[i] > old[i]) {
                tab[i] = -1;
            }
        }

        if (i == tab.length) {

            String[] sVal = new String[tab.length];
            for (int j = 0; j < tab.length; j++) {
                sVal[j] = Integer.toString(tab[j]);
            }
            return Joiner.on(NetworkState.SEPARATOR).join(sVal);
        }

        return null;
    }

    public String getCoverState(int[] state, List<String> path) {

        for (String netState : path) {

            String result = coverString(state, unpackStateString(netState));
            if (result != null) {
                return result;
            }
        }

        return null;
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
