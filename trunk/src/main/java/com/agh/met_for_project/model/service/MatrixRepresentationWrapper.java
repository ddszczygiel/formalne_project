package com.agh.met_for_project.model.service;


import java.util.List;

public class MatrixRepresentationWrapper {

    private List<List<Integer>> matrix;
    private List<String> placeSequence;
    private List<String> transitionSequence;

    public MatrixRepresentationWrapper(List<List<Integer>> matrix, List<String> placeSequence, List<String> transitionSequence) {
        this.matrix = matrix;
        this.placeSequence = placeSequence;
        this.transitionSequence = transitionSequence;
    }

    public List<List<Integer>> getMatrix() {
        return matrix;
    }

    public List<String> getPlaceSequence() {
        return placeSequence;
    }

    public List<String> getTransitionSequence() {
        return transitionSequence;
    }
}
