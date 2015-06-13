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

    @Autowired
    private PetriesNetwork petriesNetwork;

    @Autowired
    private Status status;

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

    public void setInitialNode() {

        initialNode = new NetworkState();
        for (Place p : petriesNetwork.getPlaces()) {
            initialNode.getStates().put(p.getName(), p.getState());
        }
        String statesString = initialNode.getState();
//        initialNode.getPath().add(statesString);      // FIXME: initial state is not added to list !!!
        states.add(statesString);
        allStates.add(initialNode);
    }

    public NetworkState getInitialNode() {
        return initialNode;
    }

    public void buildReachTree() {

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

        petriesNetwork.getStatus().setReachTreeStatus(Status.TreeStatus.ACTUAL);
    }

    public void executeTransition(NetworkState state, Transition t) {

        NetworkState childState = new NetworkState(state);
        Map<String, Integer> newState = t.execute(state.getStates());
        childState.getStates().putAll(newState);
        childState.setExecutedTransitionName(t.getName());
        state.getNodes().add(childState);
        String newStateString = childState.getState();
        allStates.add(childState);
        childState.getPath().add(newStateString);
        if (states.contains(newStateString)) {
            childState.setDuplicate(true);
        } else {

            states.add(newStateString);
            if (state.getPath().size() < petriesNetwork.getMaxDepth()) {
                operationQueue.add(childState);
            }
        }
    }

    public List<NetworkState> getStates() {

        return this.allStates;
    }

    public Set<String> getUniqueStates() {

        return this.states;
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
