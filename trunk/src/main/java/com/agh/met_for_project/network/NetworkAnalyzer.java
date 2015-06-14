package com.agh.met_for_project.network;

import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.*;
import com.agh.met_for_project.model.service.MatrixRepresentationWrapper;
import com.agh.met_for_project.model.service.NetworkStateWrapper;
import com.agh.met_for_project.model.service.PlaceActivityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;


@Component
public class NetworkAnalyzer {

    @Autowired
    private PetriesNetwork network;

    @Autowired
    private CoverTree coverTree;

    @Autowired
    private ReachTree reachTree;

    public MatrixRepresentationWrapper getMatrixModel() {

        List<List<Integer>> matrix = new ArrayList<>();
        List<String> transitionSequence = new ArrayList<>();
        int transitionCount = network.getTransitions().size();
        int placeCount = network.getPlaces().size();
        for (int i = 0; i < transitionCount; i++) {

            List<Integer> column = new ArrayList<>();
            for (int j = 0; j < placeCount; j++) {
                column.add(0);
            }
            matrix.add(column);
        }

        Map<String, Integer> placesMap = new LinkedHashMap<>();
        int i = 0;
        for (Place p : network.getPlaces()) {
            placesMap.put(p.getName(), i++);
        }

        i = 0;
        for (Transition t : network.getTransitions()) {

            transitionSequence.add(t.getName());
            for (Arc arc : t.getIn()) {

                int columnIndex = placesMap.get(arc.getPlaceName());
                List<Integer> column = matrix.get(i);
                column.set(columnIndex, column.get(columnIndex) - arc.getValue());
            }

            for (Arc arc : t.getOut()) {

                int columnIndex = placesMap.get(arc.getPlaceName());
                List<Integer> column = matrix.get(i);
                column.set(columnIndex, column.get(columnIndex) + arc.getValue());
            }
            i++;
        }

        return new MatrixRepresentationWrapper(matrix, new ArrayList<>(placesMap.keySet()), transitionSequence);
    }

    public Boolean isZachowawczaXD() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkState> states = reachTree.getStates();
        int[] values = states.get(0).getStatesValues();
        int sum = sumElements(values);

        for (int i = 1; i < states.size(); i++) {
            if (sum != sumElements(states.get(i).getStatesValues())) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public Boolean isBoundedness(List<String> vectorValues) throws InvalidOperationException {

        if (vectorValues.size() != network.getPlaces().size()) {
            throw new InvalidOperationException(ErrorType.INVALID_PARAMS);
        }

        int[] vector = new int[vectorValues.size()];
        try {
            for (int i = 0; i < vector.length; i++) {
                vector[i] = Integer.parseInt(vectorValues.get(i));
            }
        } catch (NumberFormatException e) {
            throw new InvalidOperationException(ErrorType.INVALID_PARAMS);
        }


        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkState> states = reachTree.getStates();
        int size = vector.length;
        int[] sumVector = new int[size];

        for (NetworkState state : states) {

            if (!state.isDuplicate()) {

                int[] actual = state.getStatesValues();
                for (int i = 0; i < size; i++) {
                    sumVector[i] = sumVector[i] + actual[i] * vector[i];
                }
            }
        }

        int firstSum = sumVector[0];
        for (int i = 1; i < size; i++) {
            if (firstSum != sumVector[i]) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public Boolean isBoundedness(int k) throws InvalidOperationException {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        for (NetworkState state : reachTree.getStates()) {

            int[] actual = state.getStatesValues();
            for (int i = 0; i < actual.length; i++) {
                if (actual[i] > k) {
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }

    public List<String> transitionActivity() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkState> states = reachTree.getStates();
        Set<String> activeTransitions = new HashSet<>();
        for (NetworkState state : states) {
            activeTransitions.add(state.getExecutedTransitionName());
        }
        activeTransitions.remove(null); // from the initial state

        return new ArrayList<>(activeTransitions);
    }

    public List<NetworkStateWrapper> getReachTree() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkStateWrapper> wrap = new ArrayList<>();
        for (NetworkState state : reachTree.getStates()) {
            wrap.add(new NetworkStateWrapper(state));
        }
        return wrap;
    }

    public List<NetworkStateWrapper> getCoverTree() {

        if (network.getStatus().getCoverTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            coverTree.buildCoverTree();
        }

        List<NetworkStateWrapper> wrap = new ArrayList<>();
        for (NetworkState state : coverTree.getStates()) {
            wrap.add(new NetworkStateWrapper(state));
        }
        return wrap;
    }

    public Boolean isReversible() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkState> states = reachTree.getStates();
        String initialState = reachTree.getInitialNode().getState();
        Set<String> initialAccessible = new HashSet<>();

        for (NetworkState state : states) {
            if (state.getPath().contains(initialState)) {   // state has initialState on its path so return is possible from all its path members !!!
                initialAccessible.addAll(state.getPath());
            }
        }

        for (NetworkState state : reachTree.getStates()) {

            boolean found = false;
            if (initialAccessible.contains(state.getState())) {
                continue;
            } else {

                Queue<NetworkState> childs = new LinkedList<>();
                childs.addAll(state.getNodes());
                Set<String> checked = new HashSet<>();
                while (childs.size() > 0) {

                    NetworkState child = childs.poll();
                    if (checked.contains(child.getState())) {
                        continue;
                    }

                    if (initialAccessible.contains(child.getState())) {

                        found = true;
                        break;
                    } else {
                        childs.addAll(child.getNodes());
                        checked.add(child.getState());
                    }
                }
            }

            if (!found) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public List<Pair> checkPlacesBoundedness() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        Map<String, Integer> placesMaxState = new HashMap<>();
        for (Place p : network.getPlaces()) {
            placesMaxState.put(p.getName(), 0);
        }

        for (NetworkState state : reachTree.getStates()) {

            for (Entry<String, Integer> entry : state.getStates().entrySet()) {
                if (placesMaxState.get(entry.getKey()) < entry.getValue()) {
                    placesMaxState.put(entry.getKey(), entry.getValue());
                }
            }
        }

        List<Pair> maxPlaceStateList = new ArrayList<>();
        for (Entry<String, Integer> entry : placesMaxState.entrySet()) {
            maxPlaceStateList.add(new Pair(entry.getKey(), entry.getValue()));
        }

        return maxPlaceStateList;
    }

    public Boolean checkNetworkLivness() {

        if (network.getStatus().getCoverTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            coverTree.buildCoverTree();
        }

        Set<String> aliveStatesChildes = new HashSet<>();
        int count = 0;

        for (NetworkState state : coverTree.getStates()) {

            if (state.getExecutedTransitions().size() == network.getTransitions().size()) {

                for (String st : state.getPath()) {
                    aliveStatesChildes.add(st);
                }
            }
        }

        for (NetworkState state : coverTree.getStates()) {

            boolean found = false;
            if (aliveStatesChildes.contains(state.getState())) {
                continue;
            } else {

                Queue<NetworkState> childs = new LinkedList<>();
                childs.addAll(state.getNodes());
                Set<String> checked = new HashSet<>();
                while (childs.size() > 0) {

                    NetworkState child = childs.poll();
                    if (checked.contains(child.getState())) {
                        continue;
                    }

                    if (aliveStatesChildes.contains(child.getState())) {

                        found = true;
                        break;
                    } else {
                        childs.addAll(child.getNodes());
                        checked.add(child.getState());
                    }
                }
            }

            if (!found) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public List<PlaceActivityWrapper> placesActivity() {

        if (network.getStatus().getReachTreeStatus() == Status.TreeStatus.NEED_UPDATE) {
            reachTree.buildReachTree();
        }

        List<NetworkState> states = reachTree.getStates();
        Map<String, PlaceActivityWrapper> placeActivity = new HashMap<>();
        for (Place p : network.getPlaces()) {
            placeActivity.put(p.getName(), new PlaceActivityWrapper(p.getName(), PlaceActivity.DEAD));
        }

        for (NetworkState state : states) {
            for (Map.Entry<String, Integer> entry : state.getStates().entrySet()) {
                if (entry.getValue() > 0) {
                    placeActivity.get(entry.getKey()).setPlaceActivity(PlaceActivity.ALIVE);
                }
            }
        }

        return new ArrayList<>(placeActivity.values());
    }

    private int sumElements(int[] array) {

        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }

        return sum;
    }

}
