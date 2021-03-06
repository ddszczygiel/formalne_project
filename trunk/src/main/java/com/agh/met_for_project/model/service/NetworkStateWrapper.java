package com.agh.met_for_project.model.service;


import com.agh.met_for_project.model.NetworkState;

public class NetworkStateWrapper {

    private String parentState;
    private String state;
    private String executedTransitionName;
    private Boolean isDuplicate;
    private Boolean isDead;

    public NetworkStateWrapper(NetworkState state) {

        this.parentState = state.getParentState();
        this.state = state.getState();
        this.executedTransitionName = state.getExecutedTransitionName();
        this.isDuplicate = state.isDuplicate();
        this.isDead = state.isDead();
    }

    public String getParentState() {
        return parentState;
    }

    public String getExecutedTransitionName() {
        return executedTransitionName;
    }

    public String getState() {
        return state;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public String toString() {
        return "NetworkStateWrapper{" +
                "parentState='" + parentState + '\'' +
                ", state='" + state + '\'' +
                ", executedTransitionName='" + executedTransitionName + '\'' +
                ", isDuplicate=" + isDuplicate +
                ", isDead=" + isDead +
                '}';
    }
}
