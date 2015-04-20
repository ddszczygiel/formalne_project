package com.agh.met_for_project.network;


import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.InArc;
import com.agh.met_for_project.model.OutArc;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class PetriesNetwork {

    private List<Place> places;
    private List<Transition> transitions;

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Place> getPlaces() {

        return places;
    }

    @PostConstruct
    public void setUpNetwork() {

        places = new ArrayList<>();
        transitions = new ArrayList<>();
    }

    public Place getPlaceByName(String name) {

        if (name == null || "".equals(name)) {
            return null;
        }
        for (Place p : places) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public Transition getTransitionByName(String name) {

        if (name == null || "".equals(name)) {
            return null;
        }
        for (Transition t : transitions) {
            if (t.getName().equals(name)) {
                return t;
            }
        }

        return null;
    }

    public void addPlace(String name, int state) throws InvalidOperationException {

        if (getPlaceByName(name) != null) {
            throw new InvalidOperationException(ErrorType.INVALID_PLACE_PARAMS);
        }
        places.add(new Place(name, state));

    }

    public void addTransition(String name, int priority) throws InvalidOperationException {

        if (getTransitionByName(name) != null) {
            throw new InvalidOperationException(ErrorType.INVALID_TRANSITION_PARAMS);
        }
        transitions.add(new Transition(name, priority));
    }

    /**
     * Type can be "IN" - means connection FROM transition TO place or "OUT" - FORM place TO transition
     * @param transitionName
     * @param placeName
     * @param type
     * @param value
     * @throws InvalidOperationException
     */
    public void addArc(String transitionName, String placeName, String type, int value) throws InvalidOperationException {

        Transition t = getTransitionByName(transitionName);
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        Place p = getPlaceByName(placeName);
        if (p == null ) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        if ( !"IN".equals(type) && !"OUT".equals(type)) {
            throw new InvalidOperationException(ErrorType.INVALID_ARC_PARAMS);
        }

        if (connectionExist(t, p.getName(), type)) {
            throw new InvalidOperationException(ErrorType.ARC_ALREADY_EXIST);
        }

        if ("IN".equals(type)) {

            InArc inArc = new InArc();
            inArc.setValue(value);
            inArc.setEnd(p);
            t.getOut().add(inArc);
        } else {

            OutArc outArc = new OutArc();
            outArc.setValue(value);
            outArc.setBegin(p);
            t.getIn().add(outArc);
        }
    }

    private boolean connectionExist(Transition t, String placeName, String type) {

        if ("IN".equals(type)) {
            for (InArc inArc : t.getOut()) {
                if (inArc.getEnd().getName().equals(placeName)) {
                    return true;
                }
            }
        } else {
            for (OutArc outArc : t.getIn()) {
                if (outArc.getBegin().getName().equals(placeName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void modifyPlaceParams(String name, int state) throws InvalidOperationException {

        Place p = getPlaceByName(name);
        if (p == null) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        p.setName(name);
        p.setState(state);
    }

    public void modifyTransitionParams(String name, int priority) throws InvalidOperationException {

        Transition t = getTransitionByName(name);
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        t.setName(name);
        t.setPriority(priority);
    }

    public void modifyArcParams(String transitionName, String placeName, int value) throws InvalidOperationException {

        Transition t = getTransitionByName(transitionName);
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        if (getPlaceByName(placeName) == null ) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        // FIXME with additional argument only one loop would be needed - to discuss
        boolean changed = false;
        for (InArc inArc : t.getOut()) {
            if (inArc.getEnd().getName().equals(placeName)) {

                inArc.setValue(value);
                changed = true;
                break;
            }
        }

        if (!changed) {
            for (OutArc outArc : t.getIn()) {
                if (outArc.getBegin().getName().equals(placeName)) {

                    outArc.setValue(value);
                    changed = true;
                    break;
                }
            }
        }

        if (!changed) {
            throw new InvalidOperationException(ErrorType.ARC_NOT_EXIST);
        }
    }

}
