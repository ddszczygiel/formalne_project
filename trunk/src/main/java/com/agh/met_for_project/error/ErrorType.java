package com.agh.met_for_project.error;


public enum ErrorType {

    // FIXME maybe additional string with message to show on gui side ??
    PLACE_NOT_EXIST,
    TRANSITION_NOT_EXIST,
    ARC_NOT_EXIST,
    INVALID_PLACE_PARAMS,
    INVALID_TRANSITION_PARAMS,
    INVALID_ARC_PARAMS,
    ARC_ALREADY_EXIST,
    INVALID_FILE_PARAMETERS,
    SAVE_NETWORK_ERROR
}
