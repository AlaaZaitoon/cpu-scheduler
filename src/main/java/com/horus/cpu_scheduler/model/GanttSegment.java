package com.horus.cpu_scheduler.model;

public class GanttSegment {
    public int pid; // -1 means IDLE
    public int start;
    public int end;

    public GanttSegment(int pid, int start, int end) {
        this.pid = pid;
        this.start = start;
        this.end = end;
    }
}
