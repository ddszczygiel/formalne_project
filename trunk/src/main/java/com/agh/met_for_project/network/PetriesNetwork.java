package com.agh.met_for_project.network;


import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.*;
import com.agh.met_for_project.model.service.ArcParams;
import com.agh.met_for_project.model.service.ModifyArcWrapper;
import com.agh.met_for_project.model.service.ModifyPlaceWrapper;
import com.agh.met_for_project.model.service.ModifyTransitionWrapper;
import com.agh.met_for_project.model.service.PossibleTransitionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class PetriesNetwork {

    @Autowired
    private Status status;

    private boolean prioritySimulation;
    private int maxDepth = 20;

    private List<Place> places;
    private List<Transition> transitions;

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Status getStatus() {
        return status;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Place> getPlaces() {

        return places;
    }

    public boolean isPrioritySimulation() {
        return prioritySimulation;
    }

    public void setPrioritySimulation(boolean prioritySimulation) {
        this.prioritySimulation = prioritySimulation;
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

    public void addPlace(Place p) throws InvalidOperationException {

        if (getPlaceByName(p.getName()) != null) {
            throw new InvalidOperationException(ErrorType.INVALID_PLACE_PARAMS);
        }

        places.add(p);
        status.networkModified();
    }

    public void addTransition(Transition t) throws InvalidOperationException {

        if (getTransitionByName(t.getName()) != null) {
            throw new InvalidOperationException(ErrorType.INVALID_TRANSITION_PARAMS);
        }

        transitions.add(t);
        status.networkModified();
    }

    /**
     * Type can be "IN" - means connection FROM transition TO place or "OUT" - FORM place TO transition
     */
    public void addArc(ArcParams arc) throws InvalidOperationException {

        Transition t = getTransitionByName(arc.getTransitionName());
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        Place p = getPlaceByName(arc.getPlaceName());
        if (p == null ) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        if ( !"IN".equals(arc.getArcType()) && !"OUT".equals(arc.getArcType())) {
            throw new InvalidOperationException(ErrorType.INVALID_ARC_PARAMS);
        }

        if (getConnectionArc(t, p.getName(), arc.getArcType()) != null) {
            throw new InvalidOperationException(ErrorType.ARC_ALREADY_EXIST);
        }

        if ("IN".equals(arc.getArcType())) {

            InArc inArc = new InArc();
            inArc.setValue(arc.getValue());
            inArc.setEnd(p);
            t.getOut().add(inArc);
        } else {

            OutArc outArc = new OutArc();
            outArc.setValue(arc.getValue());
            outArc.setBegin(p);
            t.getIn().add(outArc);
        }

        status.networkModified();
    }

    private Arc getConnectionArc(Transition t, String placeName, String type) {

        if ("IN".equals(type)) {
            for (InArc inArc : t.getOut()) {
                if (inArc.getEnd().getName().equals(placeName)) {
                    return inArc;
                }
            }
        } else {
            for (OutArc outArc : t.getIn()) {
                if (outArc.getBegin().getName().equals(placeName)) {
                    return outArc;
                }
            }
        }

        return null;
    }

    public void modifyPlaceParams(ModifyPlaceWrapper params) throws InvalidOperationException {

        Place p = getPlaceByName(params.getActualName());
        if (p == null) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        p.setName(params.getNewName());
        p.setState(params.getNewState());
        status.networkModified();
    }

    public void modifyTransitionParams(ModifyTransitionWrapper params) throws InvalidOperationException {

        Transition t = getTransitionByName(params.getActualName());
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        t.setName(params.getNewName());
        t.setPriority(params.getNewPriority());
        status.networkModified();
    }

    public void modifyArcParams(ModifyArcWrapper params) throws InvalidOperationException {

        Transition t = getTransitionByName(params.getTransitionName());
        if (t == null) {
            throw new InvalidOperationException(ErrorType.TRANSITION_NOT_EXIST);
        }

        Place p = getPlaceByName(params.getPlaceName());
        if (p == null ) {
            throw new InvalidOperationException(ErrorType.PLACE_NOT_EXIST);
        }

        Arc arc = getConnectionArc(t, params.getPlaceName(), params.getType());
        if (arc == null) {
            throw new InvalidOperationException(ErrorType.ARC_NOT_EXIST);
        }

        arc.setValue(params.getNewValue());
        status.networkModified();
    }

    public void executeTransition(String transitionName) throws InvalidOperationException {

        Transition t = getTransitionByName(transitionName);
        if (t == null) {
            throw new InvalidOperationException(ErrorType.INVALID_TRANSITION_PARAMS);
        }

        if (!t.isExecutable()) {
            throw  new InvalidOperationException(ErrorType.TRANSITION_UNEXECUTABLE);
        }

        t.execute();
        status.networkModified();
    }

    // this is method for simulation button
    public List<PossibleTransitionWrapper> possibleTransitions() {

        List<Transition> possibleTransitions = new ArrayList<>();
        List<PossibleTransitionWrapper> transitionWrapperList = new ArrayList<>();

        for (Transition t : transitions) {
            if (t.isExecutable()) {
                possibleTransitions.add(t);
            }
        }

        if (isPrioritySimulation()) {

            Transition maxPriorityTransition = getMaxPriorityTransisiotn(possibleTransitions);
            if (maxPriorityTransition != null) {
                transitionWrapperList.add(new PossibleTransitionWrapper(maxPriorityTransition.getName(), maxPriorityTransition.getPriority()));
            }
        } else {

            for (Transition t : possibleTransitions) {
                transitionWrapperList.add(new PossibleTransitionWrapper(t.getName(), t.getPriority()));
            }
        }

        return transitionWrapperList;
    }

    private Transition getMaxPriorityTransisiotn(List<Transition> transitions) {

        int maxPriority = Integer.MIN_VALUE;
        Transition maxPriorityTransition = null;

        for (Transition t : transitions) {
            if (t.getPriority() > maxPriority) {
                maxPriority = t.getPriority();
                maxPriorityTransition = t;
            }
        }

        // if null is returned, there are no executable transitions !
        return maxPriorityTransition;
    }

    // this is method for reach and cover tree
    public List<Transition> possibleTransitions(Map<String, Integer> actualPlacesStates) {

        List<Transition> possibleTransitions = new ArrayList<>();
        List<Transition> returnList = new ArrayList<>();

        for (Transition t : transitions) {
            if (t.isExecutable(actualPlacesStates)) {
                possibleTransitions.add(t);
            }
        }

        if (isPrioritySimulation()) {

            Transition maxPriorityTransition = getMaxPriorityTransisiotn(possibleTransitions);
            if (maxPriorityTransition != null) {
                returnList.add(maxPriorityTransition);
            }
            return returnList;
        }

        return possibleTransitions;
    }

}
