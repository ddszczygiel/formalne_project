package com.agh.met_for_project.network;

import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.agh.met_for_project.model.service.MatrixRepresentationWrapper;
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

}
