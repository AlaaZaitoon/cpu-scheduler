@echo off
setlocal
set "JAVA_HOME=C:\Users\Alaa Zaitoon\.jdks\temurin-25\jdk-25.0.3+9"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Starting CPU Scheduler Simulator...
echo Using JDK at: %JAVA_HOME%
echo.
echo Opening browser in 3 seconds...
start "" http://localhost:8080/

call "%~dp0mvnw.cmd" spring-boot:run

endlocal
