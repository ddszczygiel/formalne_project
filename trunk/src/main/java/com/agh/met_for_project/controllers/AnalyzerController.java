package com.agh.met_for_project.controllers;


import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.network.NetworkAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
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
    public ResponseObject isBounededness(@RequestBody int[] vector){

        ResponseObject response = new ResponseObject();
        try {
            response.setPayload(analyzer.isBoundedness(vector));
        } catch (InvalidOperationException e) {
            response.setErrorType(e.getErrorType());
        }

        return response;
    }

    @RequestMapping(value = "/zachowawczosc")
    public ResponseObject isZachowawcza() {

        ResponseObject response = new ResponseObject();
        response.setPayload(analyzer.isZachowawczaXD());

        return response;
    }

}
