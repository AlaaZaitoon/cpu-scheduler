package com.horus.cpu_scheduler.model;

import java.util.List;

public class SchedulingResult {
    public List<ProcessData> processes;
    public List<GanttSegment> ganttChart;
    public double avgWaiting;
    public double avgTurnaround;
}
