package com.horus.cpu_scheduler.service;

import com.horus.cpu_scheduler.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SchedulerService {

    private List<ProcessData> baseToRuntime(List<ProcessData> base) {
        List<ProcessData> P = new ArrayList<>();
        for (ProcessData p : base) {
            ProcessData newP = new ProcessData(p);
            newP.rt = newP.bt;
            newP.ct = -1;
            P.add(newP);
        }
        return P;
    }


    private SchedulingResult packResults(List<ProcessData> P, List<GanttSegment> gantt) {
        SchedulingResult res = new SchedulingResult();
        res.ganttChart = gantt;


        Collections.sort(P, Comparator.comparingInt(a -> a.id));
        double sumW = 0, sumT = 0;

        for (ProcessData p : P) {
            p.turnAroundTime = p.ct - p.at;
            p.waitingTime = p.turnAroundTime - p.bt;
            sumT += p.turnAroundTime;
            sumW += p.waitingTime;
        }

        res.processes = P;
        res.avgWaiting = sumW / P.size();
        res.avgTurnaround = sumT / P.size();
        return res;
    }

    // ================== 1. FCFS ==================
    public SchedulingResult FCFS(List<ProcessData> base) {
        List<ProcessData> P = baseToRuntime(base);
        List<GanttSegment> gantt = new ArrayList<>();
        P.sort((a, b) -> {
            if (a.at != b.at) return Integer.compare(a.at, b.at);
            return Integer.compare(a.id, b.id);
        });

        int t = 0;
        for (ProcessData p : P) {
            if (t < p.at) {
                gantt.add(new GanttSegment(-1, t, p.at));
                t = p.at;
            }
            gantt.add(new GanttSegment(p.id, t, t + p.bt));
            t += p.bt;
            p.ct = t;
        }
        return packResults(P, gantt);
    }

    // ================== 2. SJF (Non-Preemptive) ==================
    public SchedulingResult SJF_NP(List<ProcessData> base) {
        List<ProcessData> P = baseToRuntime(base);
        List<GanttSegment> g = new ArrayList<>();
        int n = P.size(), done = 0, t = 0;
        boolean[] finished = new boolean[n];

        while (done < n) {
            int pick = -1;
            for (int i = 0; i < n; ++i) {
                if (!finished[i] && P.get(i).at <= t) {
                    if (pick == -1 || P.get(i).bt < P.get(pick).bt || (P.get(i).bt == P.get(pick).bt && P.get(i).id < P.get(pick).id))
                        pick = i;
                }
            }
            if (pick == -1) {
                int nxt = Integer.MAX_VALUE;
                for (int i = 0; i < n; ++i) if (!finished[i]) nxt = Math.min(nxt, P.get(i).at);
                g.add(new GanttSegment(-1, t, nxt));
                t = nxt;
                continue;
            }
            ProcessData p = P.get(pick);
            g.add(new GanttSegment(p.id, t, t + p.bt));
            t += p.bt;
            p.ct = t;
            finished[pick] = true;
            done++;
        }
        return packResults(P, g);
    }

    // ================== 3. Priority (Non-Preemptive) ==================
    public SchedulingResult PRIORITY_NP(List<ProcessData> base) {
        List<ProcessData> P = baseToRuntime(base);
        List<GanttSegment> g = new ArrayList<>();
        int n = P.size(), done = 0, t = 0;
        boolean[] finished = new boolean[n];

        while (done < n) {
            int pick = -1;
            for (int i = 0; i < n; ++i) {
                if (!finished[i] && P.get(i).at <= t) {
                    if (pick == -1 || P.get(i).pr < P.get(pick).pr || (P.get(i).pr == P.get(pick).pr && P.get(i).id < P.get(pick).id))
                        pick = i;
                }
            }
            if (pick == -1) {
                int nxt = Integer.MAX_VALUE;
                for (int i = 0; i < n; ++i) if (!finished[i]) nxt = Math.min(nxt, P.get(i).at);
                g.add(new GanttSegment(-1, t, nxt));
                t = nxt;
                continue;
            }
            ProcessData p = P.get(pick);
            g.add(new GanttSegment(p.id, t, t + p.bt));
            t += p.bt;
            p.ct = t;
            finished[pick] = true;
            done++;
        }
        return packResults(P, g);
    }

    // ================== 4. Priority (Preemptive) ==================
    public SchedulingResult PRIORITY_PREEMPT(List<ProcessData> base) {
        List<ProcessData> P = baseToRuntime(base);
        List<GanttSegment> g = new ArrayList<>();
        int n = P.size(), done = 0, t = 0;
        int lastPid = -2, segStart = 0;

        while (done < n) {
            int pick = -1;
            for (int i = 0; i < n; ++i) {
                if (P.get(i).rt > 0 && P.get(i).at <= t) {
                    if (pick == -1 || P.get(i).pr < P.get(pick).pr || (P.get(i).pr == P.get(pick).pr && P.get(i).id < P.get(pick).id))
                        pick = i;
                }
            }
            if (pick == -1) {
                if (lastPid != -1) {
                    if (lastPid != -2) g.add(new GanttSegment(lastPid, segStart, t));
                    lastPid = -1;
                    segStart = t;
                }
                t++;
                continue;
            }
            ProcessData p = P.get(pick);
            if (lastPid != p.id) {
                if (lastPid != -2) g.add(new GanttSegment(lastPid, segStart, t));
                lastPid = p.id;
                segStart = t;
            }
            p.rt--;
            t++;
            if (p.rt == 0) {
                p.ct = t;
                done++;
            }
        }
        if (lastPid != -2) g.add(new GanttSegment(lastPid, segStart, t));
        return packResults(P, g);
    }

    // ================== 5. Round Robin ==================
    public SchedulingResult ROUND_ROBIN(List<ProcessData> base, int q) {
        List<ProcessData> P = baseToRuntime(base);
        List<GanttSegment> g = new ArrayList<>();
        int n = P.size(), done = 0, t = 0;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> {
            if (P.get(a).at != P.get(b).at) return Integer.compare(P.get(a).at, P.get(b).at);
            return Integer.compare(P.get(a).id, P.get(b).id);
        });
        int ai = 0;
        Queue<Integer> Q = new LinkedList<>();

        while (done < n) {
            while (ai < n && P.get(idx[ai]).at <= t) { Q.add(idx[ai]); ai++; }
            if (Q.isEmpty()) {
                if (ai < n) {
                    int next_t = P.get(idx[ai]).at;
                    if (t < next_t) g.add(new GanttSegment(-1, t, next_t));
                    t = next_t;
                    while (ai < n && P.get(idx[ai]).at <= t) { Q.add(idx[ai]); ai++; }
                    continue;
                } else break;
            }
            int pIdx = Q.poll();
            ProcessData p = P.get(pIdx);
            int start = t;
            int run = Math.min(q, p.rt);
            t += run;
            p.rt -= run;
            g.add(new GanttSegment(p.id, start, t));
            while (ai < n && P.get(idx[ai]).at <= t) { Q.add(idx[ai]); ai++; }
            if (p.rt == 0) { p.ct = t; done++; } else { Q.add(pIdx); }
        }
        return packResults(P, g);
    }
}