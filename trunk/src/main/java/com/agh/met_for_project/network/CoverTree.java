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

    private final int MAX_DEPTH = 20;

    @Autowired
    private PetriesNetwork petriesNetwork;

    private Set<String> networkStates;
//    private Map<String, Integer[]> networkStates; //FIXME check is it is not better solution !!
    private NetworkState initialNode;
    private List<Transition> networkTransitions;
    private Queue<NetworkState> operationQueue;


    @PostConstruct
    public void setUp() {

        networkStates = new HashSet<>();
        operationQueue = new LinkedList<>();
        networkTransitions = petriesNetwork.getTransitions();
    }

    public void setInitialNode() {

        initialNode = new NetworkState();
        for (Place p : petriesNetwork.getPlaces()) {
            initialNode.getStates().put(p.getName(), p.getState());
        }
        networkStates.add(initialNode.getStatesString());
    }

    public void buildCoverTree() {

        operationQueue.add(initialNode);

        while (!operationQueue.isEmpty()) {

            NetworkState state = operationQueue.peek();     // can not be null because of while condition
            boolean noActiveTransitions = true;

            for (Transition t : networkTransitions) {

                if (t.isExecutable(state.getStates())) {

                    noActiveTransitions = false;
                    NetworkState childState = new NetworkState();
                    state.getNodes().add(childState);
                    operationQueue.add(childState);
                    Map<String, Integer> newState = t.execute(state.getStates());
                    childState.getStates().putAll(newState);
                    childState.setExecutedTransitionName(t.getName());
                    String newStatesString = childState.getStatesString();

                    if (networkStates.contains(newStatesString)) {
                        childState.setDuplicate(true);
                    } else {
                        networkStates.add(getCoverStateString(childState.getStatesValues()));
                        // FIXME when there is a cover need to change childState states map !!!
                    }
                }
            }

            if (noActiveTransitions) {
                state.setDead(true);
            }
        }
    }

    private int[] unpackStateString(String state) {

        String[] values = state.split(NetworkState.SEPARATOR);
        int[] tab = new int[values.length];
        for (int i=0; i<values.length; i++) {
            tab[i] = Integer.parseInt(values[i]);
        }

        return tab;
    }

    // FIXME check condition for comparing states with infinity = -1
    private String coverString(int[] cover, int[] old) {

        int[] tab = cover.clone();
        int i;
        for (i=0; i<tab.length; i++) {

            if (tab[i]<old[i]) {
                break;
            } else if (tab[i]>old[i]) {
                tab[i] = -1;
            }
        }

        return (i == tab.length) ? Joiner.on(NetworkState.SEPARATOR).join(Arrays.asList(tab)) : null;
    }

    public String getCoverStateString(int[] state) {

        for (String netState : networkStates) {

            String result = coverString(state, unpackStateString(netState));
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
