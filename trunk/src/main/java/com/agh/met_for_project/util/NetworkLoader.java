package com.agh.met_for_project.util;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NetworkLoader {

    @Autowired
    private PetriesNetwork petriesNetwork;

    private final String NETWORK_FILE = "/network.txt";
    private final String CONNECTION_LINE_PATTERN = "(\\w+) (\\w+) (\\w+) (\\d+) (\\d+)";

    public void loadNetwork(String filePath) {

        List<String> parsed = getLinesFromFile(filePath);
        if (!parsed.isEmpty()) {

            petriesNetwork.setUpNetwork();
            int placeCount = Integer.parseInt(parsed.get(0));
            int transitionCount = Integer.parseInt(parsed.get(1));
            prepareNetworkElements(placeCount, transitionCount);
            assignInitialStates(parsed.get(2).split(" "));
            Pattern connectionPattern = Pattern.compile(CONNECTION_LINE_PATTERN);
            for (int i=3; i<parsed.size(); i++) {

                Matcher match = connectionPattern.matcher(parsed.get(i));
                match.find();
                createConnection(match);
            }
        } else {
            //TODO to do
        }
    }

    private void prepareNetworkElements(int placeCount, int transitionCount) {

        for (int i=0; i<placeCount; i++) {
            petriesNetwork.getPlaces().add(new Place());
        }

        for (int i=0; i<transitionCount; i++) {
            petriesNetwork.getTransitions().add(new Transition());
        }
    }

    private void assignInitialStates(String[] states) {

        for (int i=0; i<states.length; i++) {
            petriesNetwork.getPlaces().get(i).setState(Integer.parseInt(states[i]));
        }
    }

    private void createConnection(Matcher match) {

        int from = Integer.parseInt(match.group(1));
        int to = Integer.parseInt(match.group(2));
        int transitionNo = Integer.parseInt(match.group(3));
        Place p1 = petriesNetwork.getPlaces().get(from - 1);
        Place p2 = petriesNetwork.getPlaces().get(to - 1);
        Transition transition = petriesNetwork.getTransitions().get(transitionNo - 1);

        OutArc outArc = new OutArc();
        outArc.setEnd(transition);
        outArc.setBegin(p1);
        outArc.setValue(Integer.parseInt(match.group(4)));

        InArc inArc = new InArc();
        inArc.setBegin(transition);
        inArc.setEnd(p2);
        inArc.setValue(Integer.parseInt(match.group(5)));

        transition.getIn().add(outArc);
        transition.getOut().add(inArc);
    }

    public void saveNetwork() {

        List<String> lines = new ArrayList<>();
        lines.add(Integer.toString(petriesNetwork.getPlaces().size()));
        lines.add(Integer.toString(petriesNetwork.getTransitions().size()));
        List<String> initialStates = new ArrayList<>();
        for (Place p : petriesNetwork.getPlaces()) {
            initialStates.add(Integer.toString(p.getState()));
        }
        lines.add(Joiner.on(" ").join(initialStates));

        for (Transition t : petriesNetwork.getTransitions()) {
            for (OutArc outArc : t.getIn()) {
                for (InArc inArc : t.getOut()) {
                    lines.add(String.format("%s %s %s %d %d", outArc.getBegin().getName(), inArc.getEnd().getName(),
                            t.getName(), outArc.getValue(), inArc.getValue()));
                }
            }
        }

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public void saveNetworkToFile(List<String> network) {

        try {

            BufferedWriter file = new BufferedWriter(new FileWriter("net.txt"));
            for (String val : network) {
                file.write(val);
                file.newLine();
            }
            file.close();
        } catch (IOException e) {
            //FIXME ???
        }
    }

}
