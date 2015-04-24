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

    private static final String SEPARATOR = ",";

    private String executedTransitionName;
    private Map<String, Integer> states;
    private List<NetworkState> nodes;
    private boolean duplicate;
    private boolean dead;

    public NetworkState() {

        states = new TreeMap<>();   // to provide the same order of places in every new NetworkState
        nodes = new ArrayList<>();
    }

    public String getStatesString() {

        return Joiner.on(SEPARATOR).join(states.values());
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

    public NetworkState setDead(boolean dead) {
        this.dead = dead;
        return this;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public NetworkState setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
        return this;
    }

    public String toString() {

        return String.format("STATE: %s\nEXECUTED TRANSITION: %s\nDUPLICATE %s\nDEAD %s\n", getStatesString(), executedTransitionName, duplicate, dead);
    }

}
