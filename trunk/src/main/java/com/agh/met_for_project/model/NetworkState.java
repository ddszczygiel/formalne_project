package com.agh.met_for_project.model;


import com.google.common.base.Joiner;

import java.util.*;

/**
 * 1. Original network states would be changed only in case of user interaction form GUI
 * 2. Transitions are not changing their parameters during whole process of creating cover/reach tree
 * 3. To provide cover/reach tree, during simulation, after executing transition, new tree state must be somehow stored.
 *    It is done using "states" map that contains place name as key and its state after execution as value.
 *    Nie wiem po co pisze to po angielsku ale spoko ... pocwicze sobie
 */
public class NetworkState {

    public static final String SEPARATOR = ",";

    private String parentState;
    private String executedTransitionName;
    private Map<String, Integer> states;
    private List<NetworkState> nodes;
    private List<String> path;
    private Set<String> executedTransitions;
    private boolean duplicate;
    private boolean dead;

    public NetworkState() {

        states = new LinkedHashMap<>();   // to provide the same order of places in every new NetworkState as in Places list
        path = new ArrayList<>();
        executedTransitions = new HashSet<>();
        nodes = new ArrayList<>();
    }

    public NetworkState(NetworkState parent) {

        this();
        parentState = parent.getState();
        path.addAll(parent.getPath());
        executedTransitions.addAll(parent.getExecutedTransitions());
    }

    public int[] getStatesValues() {

        int[] tab = new int[states.size()];
        int i = 0;
        for (Integer val : states.values()) {
            tab[i++] = val;
        }

        return tab;
    }

    // FIXME check if needed
    public String getState() {

        return Joiner.on(SEPARATOR).join(states.values());
    }

    public String getParentState() {
        return parentState;
    }

    public void setParentState(String parentState) {
        this.parentState = parentState;
    }

    public List<String> getPath() {
        return path;
    }

    public Set<String> getExecutedTransitions() {
        return executedTransitions;
    }

    public String getExecutedTransitionName() {
        return executedTransitionName;
    }

    public void setExecutedTransitionName(String executedTransitionName) {
        this.executedTransitionName = executedTransitionName;
    }

    public List<NetworkState> getNodes() {
        return nodes;
    }

    public void setNodes(List<NetworkState> nodes) {
        this.nodes = nodes;
    }

    public Map<String, Integer> getStates() {
        return states;
    }

    public void setStates(Map<String, Integer> states) {
        this.states = states;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public String toString() {

        return String.format("PARENT: %s\nSTATE: %s\nEXECUTED TRANSITION: %s\nDUPLICATE %s\nDEAD %s\n",
                parentState, getState(), executedTransitionName, duplicate, dead);
    }

}
