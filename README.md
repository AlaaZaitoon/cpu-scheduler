# 🖥️ CPU Scheduling Simulator

A high-performance **CPU Scheduling Simulator** designed using **Java Spring Boot (Java 25)** on the backend and a premium, responsive **Vanilla HTML/CSS/JS** interface on the frontend. 

🔗 **Live Deployment Link:** **[https://cpu-scheduler-9c10.onrender.com/](https://cpu-scheduler-9c10.onrender.com/)**

It simulates various CPU scheduling algorithms, visualizes execution sequences using an interactive **Gantt Chart**, and calculates key performance metrics such as **Completion Time (CT)**, **Turnaround Time (TAT)**, and **Waiting Time (WT)**.

---

## 🚀 Key Features

* **5 Scheduling Algorithms Supported:**
  * **First Come First Served (FCFS)**
  * **Shortest Job First (SJF)** (Non-Preemptive)
  * **Priority Scheduling** (Non-Preemptive)
  * **Priority Scheduling** (Preemptive)
  * **Round Robin (RR)** (with custom Time Quantum)
* **Interactive Visualizations:** Dynamically renders color-coded Gantt Charts with precise timing intervals.
* **Instant Simulator Performance Metrics:** Computes Average Turnaround Time and Average Waiting Time on the fly.
* **Pre-loaded Sample Data:** Comes with default initial processes so you can test any algorithm immediately with a single click.
* **Launch Script included:** A custom `run.bat` script boots up the server and opens the simulator UI automatically.
* **Fully Unit Tested:** Core scheduling services are backed by a robust JUnit 5 integration test suite.

---

## 🛠️ Technology Stack

* **Backend:** Spring Boot (MVC architecture), Java 25, Maven
* **Frontend:** Responsive HTML5, Modern CSS, Vanilla ES6 JavaScript
* **Testing:** JUnit 5, Mockito, Spring Boot Test

---

## ⚡ Quick Start

### 1. Launch with One Click (Windows)
We've included a standalone `run.bat` launch script. Simply **double-click `run.bat`** in the project's root folder. 
It will:
1. Automatically set up the custom JDK 25.
2. Open your default web browser to [http://localhost:8080/](http://localhost:8080/).
3. Start the Java dev server.

### 2. Manual Run
If you prefer running via a terminal:
```powershell
$env:JAVA_HOME = "C:\Users\Alaa Zaitoon\.jdks\temurin-25\jdk-25.0.3+9"
.\mvnw.cmd spring-boot:run
```

---

## 🧪 Testing

The core scheduling algorithms are validated for mathematical correctness against standard scheduling problems. To run the automated unit test suite:

```powershell
$env:JAVA_HOME = "C:\Users\Alaa Zaitoon\.jdks\temurin-25\jdk-25.0.3+9"
.\mvnw.cmd test
```

---

## 📈 Preview of Calculations

The simulator calculates metrics using the following operating system formulas:

$$\text{Turnaround Time (TAT)} = \text{Completion Time (CT)} - \text{Arrival Time (AT)}$$

$$\text{Waiting Time (WT)} = \text{Turnaround Time (TAT)} - \text{Burst Time (BT)}$$
