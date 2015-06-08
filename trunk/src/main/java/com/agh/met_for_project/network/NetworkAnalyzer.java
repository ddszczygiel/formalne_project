package com.agh.met_for_project.network;

import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.NetworkState;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.agh.met_for_project.model.service.MatrixRepresentationWrapper;
import com.agh.met_for_project.model.service.NetworkStateWrapper;
import com.agh.met_for_project.model.service.PlaceActivity;
import com.agh.met_for_project.model.service.PlaceActivityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


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
            for (int i=0; i<vector.length; i++) {
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

            int[] actual = state.getStatesValues();
            for (int i=0; i<size; i++) {
                sumVector[i] = sumVector[i] + actual[i]*vector[i];
            }
        }

        int firstSum = sumVector[0];
        for (int i=1; i<size; i++) {
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
            for (int i=0; i<actual.length; i++) {
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
                for (String pathMember: state.getPath()) {
                    initialAccessible.add(pathMember);
                }
            }
        }

        if (initialAccessible.size() == reachTree.getUniqueStates().size()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
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
