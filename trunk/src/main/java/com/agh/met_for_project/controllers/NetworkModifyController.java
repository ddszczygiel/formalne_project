package com.agh.met_for_project.controllers;

import com.agh.met_for_project.error.ErrorType;
import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.service.*;
import com.agh.met_for_project.model.Place;
import com.agh.met_for_project.model.Transition;
import com.agh.met_for_project.network.PetriesNetwork;
import com.agh.met_for_project.util.NetworkLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;


@RestController
@RequestMapping(value="/api")
public class NetworkModifyController {

    @Autowired
    private PetriesNetwork petriesNetwork;

    @Autowired
    private NetworkLoader networkLoader;

    @RequestMapping(value = "/addplace", method = RequestMethod.POST)
    public ResponseObject addPlace(@RequestBody Place p) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addPlace(p);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/addtransition", method = RequestMethod.POST)
    public ResponseObject addTransition(@RequestBody Transition t) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addTransition(t);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/addarc", method = RequestMethod.POST)
    public ResponseObject addArc(@RequestBody ArcParams arc) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addArc(arc);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/addallplaces", method = RequestMethod.POST)
    public ResponseObject addAllPlaces(@RequestBody List<Place> places) {

        ResponseObject responseObject = new ResponseObject();
        try {
            for (Place p : places) {
                petriesNetwork.addPlace(p);
            }
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/addalltransitions", method = RequestMethod.POST)
    public ResponseObject addAllTransitions(@RequestBody List<Transition> transitions) {

        ResponseObject responseObject = new ResponseObject();
        try {
            for (Transition t : transitions) {
                petriesNetwork.addTransition(t);
            }
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/addallarcs", method = RequestMethod.POST)
    public ResponseObject addAllArcs(@RequestBody List<ArcParams> arcs) {

        ResponseObject responseObject = new ResponseObject();
        try {
            for (ArcParams arc : arcs) {
                petriesNetwork.addArc(arc);
            }
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/modifyplace", method = RequestMethod.POST)
    public ResponseObject modifyPlace(@RequestBody ModifyPlaceWrapper params) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyPlaceParams(params);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/modifytransition", method = RequestMethod.POST)
    public ResponseObject modifyTransition(@RequestBody ModifyTransitionWrapper params) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyTransitionParams(params);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping(value = "/modifyarc", method = RequestMethod.POST)
    public ResponseObject modifyArc(@RequestBody ModifyArcWrapper params) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyArcParams(params);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/network")
    public ResponseObject getNetwork() {

        ResponseObject responseObject = new ResponseObject();
        responseObject.setPayload(new NetworkWrapper(petriesNetwork));

        return responseObject;
    }

    @RequestMapping("/executetransition")
    public ResponseObject executeTransition(@RequestBody String transitionName) {
    //public ResponseObject executeTransition(@RequestParam String transitionName) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.executeTransition(transitionName);
            responseObject.setPayload(new NetworkWrapper(petriesNetwork));
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/possibletransitions")
    public ResponseObject getPossibleTransitions() {

        ResponseObject responseObject = new ResponseObject();
        responseObject.setPayload(petriesNetwork.possibleTransitions());

        return responseObject;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseObject uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) throws InvalidOperationException {

        ResponseObject responseObject = new ResponseObject();
        try {
            networkLoader.loadNetwork(file.getInputStream());
            responseObject.setPayload(Boolean.TRUE);
        } catch (IOException | InvalidOperationException e) {
            responseObject.setErrorType(ErrorType.LOAD_NETWORK_ERROR);
        }

        return responseObject;
    }

    @RequestMapping("/prioritysimulation")
    public ResponseObject setPriorityMode(@RequestBody boolean val) {

        ResponseObject responseObject = new ResponseObject();
        petriesNetwork.setPrioritySimulation(val);
        responseObject.setPayload(Boolean.TRUE);

        return responseObject;
    }

    @RequestMapping("/maxdepthvalue")
    public ResponseObject setMaxDepth(@RequestBody int depth) {

        ResponseObject responseObject = new ResponseObject();
        petriesNetwork.setMaxDepth(depth);
        responseObject.setPayload(Boolean.TRUE);

        return responseObject;
    }


}
