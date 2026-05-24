package com.horus.cpu_scheduler.model;

public class ProcessData {
    public int id;
    public int at;   // arrival time
    public int bt;   // burst time
    public int pr;   // priority
    public int rt;   // remaining time
    public int ct;   // completion time
    public int waitingTime;
    public int turnAroundTime;

    public ProcessData() {}

    public ProcessData(int id, int at, int bt, int pr) {
        this.id = id;
        this.at = at;
        this.bt = bt;
        this.pr = pr;
        this.rt = bt;
    }

    public ProcessData(ProcessData other) {
        this.id = other.id;
        this.at = other.at;
        this.bt = other.bt;
        this.pr = other.pr;
        this.rt = other.rt;
        this.ct = other.ct;
    }
}