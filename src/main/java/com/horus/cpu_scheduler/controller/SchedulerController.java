package com.horus.cpu_scheduler.controller;

import com.horus.cpu_scheduler.model.*;
import com.horus.cpu_scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SchedulerController {

    @Autowired
    private SchedulerService service;

    @GetMapping("/ping")
    public String ping() {
        return "UP";
    }

    @PostMapping("/solve")
    public SchedulingResult solve(
            @RequestBody List<ProcessData> processes,
            @RequestParam String algo,
            @RequestParam(defaultValue = "2") int quantum) {

        switch (algo) {
            case "FCFS":
                return service.FCFS(processes);
            case "SJF_NP":
                return service.SJF_NP(processes);
            case "PRIORITY_NP":
                return service.PRIORITY_NP(processes);
            case "PRIORITY_PREEMPT":
                return service.PRIORITY_PREEMPT(processes);
            case "RR":
                return service.ROUND_ROBIN(processes, quantum);
            default:
                return null;
        }
    }
}