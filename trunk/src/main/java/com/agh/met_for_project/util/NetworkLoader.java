package com.agh.met_for_project.util;


import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
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

    public void loadNetwork(String filePath) throws InvalidOperationException {

        List<String> parsed = getLinesFromFile(filePath);
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
            petriesNetwork.addPlace(params[0], Integer.parseInt(params[1]));
        }
    }

    private void prepareTransitions(List<String> parsed, int start, int end) throws InvalidOperationException {

        for (int i = start; i<end; i++) {

            String[] params = parsed.get(i).split(VALUES_SEPARATOR);
            petriesNetwork.addTransition(params[0], Integer.parseInt(params[1]));
        }
    }

    private void createConnections(List<String> parsed, int start) throws InvalidOperationException {


        for (int i = start; i<parsed.size(); i++) {

            String[] params = parsed.get(i).split(VALUES_SEPARATOR);
            if (params.length != 5) {
                throw new InvalidOperationException(ErrorType.INVALID_FILE_PARAMETERS);
            }

            Place begin = petriesNetwork.getPlaceByName(params[0]);
            Place end = petriesNetwork.getPlaceByName(params[1]);
            Transition t = petriesNetwork.getTransitionByName(params[2]);

            if ((begin == null) || (end == null)) {
                throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
            }

            if (t == null) {
                throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
            }

            OutArc outArc = new OutArc();
            outArc.setBegin(begin);
            outArc.setValue(Integer.parseInt(params[3]));
            InArc inArc = new InArc();
            inArc.setEnd(end);
            inArc.setValue(Integer.parseInt(params[4]));
            t.getIn().add(outArc);
            t.getOut().add(inArc);
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
            for (OutArc outArc : t.getIn()) {
                for (InArc inArc : t.getOut()) {

                    connectionLines.add(Joiner.on(VALUES_SEPARATOR).join(new String[] { outArc.getBegin().getName(), inArc.getEnd().getName(),
                            t.getName(), Integer.toString(outArc.getValue()), Integer.toString(inArc.getValue()) }));
                }
            }
        }
        lines.addAll(connectionLines);
        saveNetworkToFile(lines);
    }

    public List<String> getLinesFromFile(String filePath) {

        List<String> lines = new ArrayList<>();
        try {

            BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(NETWORK_FILE)));
//            BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
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
