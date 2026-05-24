package com.horus.cpu_scheduler;

import com.horus.cpu_scheduler.model.*;
import com.horus.cpu_scheduler.service.SchedulerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CpuSchedulerApplicationTests {

	@Autowired
	private SchedulerService schedulerService;

	@Test
	void contextLoads() {
		assertNotNull(schedulerService);
	}

	@Test
	void testAlgorithms() {
		// P1: AT=0, BT=8, PR=3
		// P2: AT=1, BT=4, PR=1
		// P3: AT=2, BT=9, PR=4
		// P4: AT=3, BT=5, PR=2
		List<ProcessData> base = Arrays.asList(
			new ProcessData(1, 0, 8, 3),
			new ProcessData(2, 1, 4, 1),
			new ProcessData(3, 2, 9, 4),
			new ProcessData(4, 3, 5, 2)
		);

		// 1. FCFS
		SchedulingResult fcfs = schedulerService.FCFS(base);
		assertEquals(8, fcfs.processes.get(0).ct);  // P1
		assertEquals(12, fcfs.processes.get(1).ct); // P2
		assertEquals(21, fcfs.processes.get(2).ct); // P3
		assertEquals(26, fcfs.processes.get(3).ct); // P4
		assertEquals(8.75, fcfs.avgWaiting, 0.001);
		assertEquals(15.25, fcfs.avgTurnaround, 0.001);

		// 2. SJF_NP
		SchedulingResult sjf = schedulerService.SJF_NP(base);
		assertEquals(8, sjf.processes.get(0).ct);  // P1
		assertEquals(12, sjf.processes.get(1).ct); // P2
		assertEquals(26, sjf.processes.get(2).ct); // P3
		assertEquals(17, sjf.processes.get(3).ct); // P4

		// 3. PRIORITY_NP
		SchedulingResult prNp = schedulerService.PRIORITY_NP(base);
		assertEquals(8, prNp.processes.get(0).ct);  // P1
		assertEquals(12, prNp.processes.get(1).ct); // P2
		assertEquals(26, prNp.processes.get(2).ct); // P3
		assertEquals(17, prNp.processes.get(3).ct); // P4

		// 4. PRIORITY_PREEMPT
		SchedulingResult prPre = schedulerService.PRIORITY_PREEMPT(base);
		assertEquals(17, prPre.processes.get(0).ct); // P1
		assertEquals(5, prPre.processes.get(1).ct);  // P2
		assertEquals(26, prPre.processes.get(2).ct); // P3
		assertEquals(10, prPre.processes.get(3).ct); // P4

		// 5. ROUND_ROBIN
		SchedulingResult rr = schedulerService.ROUND_ROBIN(base, 2);
		assertEquals(22, rr.processes.get(0).ct); // P1
		assertEquals(12, rr.processes.get(1).ct); // P2
		assertEquals(26, rr.processes.get(2).ct); // P3
		assertEquals(23, rr.processes.get(3).ct); // P4
	}
}
