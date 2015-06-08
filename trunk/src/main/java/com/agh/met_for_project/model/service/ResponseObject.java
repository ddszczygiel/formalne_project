package com.agh.met_for_project.model.service;


import com.agh.met_for_project.error.ErrorType;


public class ResponseObject {

    private Object payload;
    private ErrorType errorType;

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
