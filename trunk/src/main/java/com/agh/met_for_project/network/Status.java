package com.agh.met_for_project.network;


import org.springframework.stereotype.Component;

@Component
public class Status {

    public enum TreeStatus {

        ACTUAL,
        NEED_UPDATE
    }

    private TreeStatus coverTreeStatus;
    private TreeStatus reachTreeStatus;

    public Status() {

        coverTreeStatus = TreeStatus.NEED_UPDATE;
        reachTreeStatus = TreeStatus.NEED_UPDATE;
    }

    public void setCoverTreeStatus(TreeStatus coverTreeStatus) {
        this.coverTreeStatus = coverTreeStatus;
    }

    public void setReachTreeStatus(TreeStatus reachTreeStatus) {
        this.reachTreeStatus = reachTreeStatus;
    }

    public TreeStatus getCoverTreeStatus() {
        return coverTreeStatus;
    }

    public TreeStatus getReachTreeStatus() {
        return reachTreeStatus;
    }

    public void networkModified() {

        coverTreeStatus = TreeStatus.NEED_UPDATE;
        reachTreeStatus = TreeStatus.NEED_UPDATE;
    }
}
