package com.agh.met_for_project.controllers;


import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.service.ResponseObject;
import com.agh.met_for_project.network.NetworkAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class AnalyzerController {

    @Autowired
    private NetworkAnalyzer analyzer;

    @RequestMapping(value = "/reachtree")
    public ResponseObject getReachTree() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.getReachTree());

        return response;
    }

    @RequestMapping(value = "/covertree")
    public ResponseObject getCoverTree() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.getCoverTree());

        return response;
    }

    @RequestMapping(value = "/matrixmodel")
    public ResponseObject getMatrixModel() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.getMatrixModel());

        return response;
    }

    @RequestMapping(value = "/bounededness")
    public ResponseObject isBounededness(@RequestBody int k) {

        ResponseObject response = new ResponseObject();
        try {
            response.setPayload(analyzer.isBoundedness(k));
        } catch (InvalidOperationException e) {
            response.setErrorType(e.getErrorType());
        }

        return response;
    }

    @RequestMapping(value = "/vectorbounededness")
    public ResponseObject isBounededness(@RequestBody List<String> valuesVector) {

        ResponseObject response = new ResponseObject();
        try {
            response.setPayload(analyzer.isBoundedness(valuesVector));
        } catch (InvalidOperationException e) {
            response.setErrorType(e.getErrorType());
        }

        return response;
    }

    @RequestMapping(value = "/safe")
    public ResponseObject isSafe() {

        ResponseObject response = new ResponseObject();
        try {
            response.setPayload(analyzer.isBoundedness(1));
        } catch (InvalidOperationException e) {
            response.setErrorType(e.getErrorType());
        }

        return response;
    }

    @RequestMapping(value = "/reversibility")
    public ResponseObject isReversable() {

        ResponseObject responseObject = new ResponseObject();
        responseObject.setPayload(analyzer.isReversible());

        return responseObject;
    }

    @RequestMapping(value = "/zachowawczosc")
    public ResponseObject isZachowawcza() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.isZachowawczaXD());

        return response;
    }

    @RequestMapping(value = "/activetransitions")
    public ResponseObject getActiveTransitions() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.transitionActivity());

        return response;
    }

    @RequestMapping(value = "/maxplacesstate")
    public ResponseObject getMaxPlacesState() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.checkPlacesBoundedness());

        return response;
    }

    @RequestMapping(value = "/networklivness")
    public ResponseObject networkLivness() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.checkNetworkLivness());

        return response;
    }

    @RequestMapping(value = "/activeplaces")
    public ResponseObject getActivePlaces() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.placesActivity());

        return response;
    }

}
