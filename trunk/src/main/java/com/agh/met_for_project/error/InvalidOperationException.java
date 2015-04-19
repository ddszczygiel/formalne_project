package com.agh.met_for_project.error;



public class InvalidOperationException extends Exception {

    private final ErrorType errorType;

    public InvalidOperationException(ErrorType errorType) {
        super(errorType.name());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
