package com.agh.met_for_project.util;


import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.Arc;
import com.agh.met_for_project.model.InArc;
import com.agh.met_for_project.model.OutArc;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.agh.met_for_project.network.PetriesNetwork;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NetworkLoader {

    @Autowired
    private PetriesNetwork petriesNetwork;

    private final String NETWORK_FILE = "/network.txt";
    private final String VALUES_SEPARATOR = ";";

    public void loadNetwork(InputStream inputStream) throws InvalidOperationException {

        List<String> parsed = getLinesFromFile(inputStream);
        try {
            if (!parsed.isEmpty()) {

                petriesNetwork.setUpNetwork();
                int placeCount = Integer.parseInt(parsed.get(0));
                int transitionCount = Integer.parseInt(parsed.get(1));
                int[] placesInterval = new int[]{2, 2 + placeCount};
                int[] transitionsInterval = new int[]{2 + placeCount, 2 + placeCount + transitionCount};
                preparePlaces(parsed, placesInterval[0], placesInterval[1]);
                prepareTransitions(parsed, transitionsInterval[0], transitionsInterval[1]);
                createConnections(parsed, transitionsInterval[1]);

                return;
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {}

        throw new InvalidOperationException(ErrorType.INVALID_FILE_PARAMETERS);
    }

    private void preparePlaces(List<String> parsed, int start, int end) throws InvalidOperationException {

        for (int i = start; i<end; i++) {
            String[] params = parsed.get(i).split(VALUES_SEPARATOR);
            petriesNetwork.addPlace(new Place(params[0], Integer.parseInt(params[1])));
        }
    }

    private void prepareTransitions(List<String> parsed, int start, int end) throws InvalidOperationException {

        for (int i = start; i<end; i++) {

            String[] params = parsed.get(i).split(VALUES_SEPARATOR);
            petriesNetwork.addTransition(new Transition(params[0], Integer.parseInt(params[1])));
        }
    }

    private void createConnections(List<String> parsed, int start) throws InvalidOperationException {

        for (int i = start; i<parsed.size(); i++) {

            String[] params = parsed.get(i).split(VALUES_SEPARATOR);
            if (params.length != 4 || !("I".equals(params[0]) || "O".equals(params[0]))) {
                throw new InvalidOperationException(ErrorType.INVALID_FILE_PARAMETERS);
            }

            String arcType = params[0];
            Transition t = petriesNetwork.getTransitionByName(params[1]);
            Place p = petriesNetwork.getPlaceByName(params[2]);

            if (p == null) {
                throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
            }

            if (t == null) {
                throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
            }

            if ("I".equals(arcType)) {

                InArc inArc = new InArc();
                inArc.setEnd(p);
                inArc.setValue(Integer.parseInt(params[3]));
                t.getOut().add(inArc);
            } else {

                OutArc outArc = new OutArc();
                outArc.setBegin(p);
                outArc.setValue(Integer.parseInt(params[3]));
                t.getIn().add(outArc);
            }

        }
    }

    public void saveNetwork() throws InvalidOperationException {

        List<String> lines = new ArrayList<>();
        lines.add(Integer.toString(petriesNetwork.getPlaces().size()));
        lines.add(Integer.toString(petriesNetwork.getTransitions().size()));
        for (Place p : petriesNetwork.getPlaces()) {
            lines.add(String.format("%s%s%s", p.getName(), VALUES_SEPARATOR, Integer.toString(p.getState())));
        }

        List<String> connectionLines = new ArrayList<>();
        for (Transition t : petriesNetwork.getTransitions()) {
            lines.add(String.format("%s%s%s", t.getName(), VALUES_SEPARATOR, Integer.toString(t.getPriority())));

            for (Arc arc : t.getIn()) {
                connectionLines.add(Joiner.on(VALUES_SEPARATOR).join(new String[] {"O", t.getName(),
                        arc.getPlaceName(), String.valueOf(arc.getValue())}));
            }

            for (Arc arc : t.getOut()) {
                connectionLines.add(Joiner.on(VALUES_SEPARATOR).join(new String[] {"I", t.getName(),
                        arc.getPlaceName(), String.valueOf(arc.getValue())}));
            }
        }
        lines.addAll(connectionLines);
        saveNetworkToFile(lines);
    }

    private List<String> getLinesFromFile(InputStream inputStream) {

        List<String> lines = new ArrayList<>();
        try {

//            BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(NETWORK_FILE)));
            BufferedReader file = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = file.readLine()) != null) {
                lines.add(line);
            }
            file.close();
        } catch (IOException e) {}

        return lines;
    }

    private void saveNetworkToFile(List<String> network) throws InvalidOperationException {

        try {

            BufferedWriter file = new BufferedWriter(new FileWriter("net.txt"));
            for (String val : network) {

                file.write(val);
                file.newLine();
            }
            file.close();
        } catch (IOException e) {
            throw new InvalidOperationException(ErrorType.SAVE_NETWORK_ERROR);
        }
    }

}
