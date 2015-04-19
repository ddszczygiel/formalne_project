package com.agh.met_for_project.controllers;

import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.network.PetriesNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NetworkModifyController {

    @Autowired
    private PetriesNetwork petriesNetwork;

    @RequestMapping("/addplace")
    public ResponseObject addPlace(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "state") int state) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addPlace(name, state);
            responseObject.setPayload(Boolean.TRUE);    //FIXME returning true when no error ???
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/addtransition")
    public ResponseObject addTransition(@RequestParam(value="name") String name,
                                        @RequestParam(value="priority") int priority) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addTransition(name, priority);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/addarc")
    public ResponseObject addArc(@RequestParam(value="transitionName") String transitionName,
                                 @RequestParam(value="placeName") String placeName,
                                 @RequestParam(value="arctype") String type,
                                 @RequestParam(value="value") int value) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.addArc(transitionName, placeName, type, value);
            responseObject.setPayload(Boolean.TRUE);    //FIXME returning true when no error ???
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/modifyplace")
    public ResponseObject modifyPlace(@RequestParam(value="name") String name,
                                      @RequestParam(value="state") int state) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyPlaceParams(name, state);
            responseObject.setPayload(Boolean.TRUE);    //FIXME returning true when no error ???
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/modifytransition")
    public ResponseObject modifyTransition(@RequestParam(value="name") String name,
                                           @RequestParam(value="priority") int priority) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyTransitionParams(name, priority);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/modifyarc")
    public ResponseObject modifyArc(@RequestParam(value="transitionName") String transitionName,
                                    @RequestParam(value="placeName") String placeName,
                                    @RequestParam(value="value") int value) {

        ResponseObject responseObject = new ResponseObject();
        try {
            petriesNetwork.modifyArcParams(transitionName, placeName, value);
            responseObject.setPayload(Boolean.TRUE);
        } catch (InvalidOperationException e) {
            responseObject.setErrorType(e.getErrorType());
        }

        return responseObject;
    }

    @RequestMapping("/network")
    public ResponseObject getNetwork() {

        ResponseObject responseObject = new ResponseObject();
        responseObject.setPayload(petriesNetwork);

        return responseObject;
    }

}
