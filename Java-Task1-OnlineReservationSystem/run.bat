@echo off
REM Script to compile and run OASIS INFOBYTE Task 1 - Online Reservation System on Windows

echo ==========================================================
echo   OASIS INFOBYTE - Task 1: Online Reservation System
echo ==========================================================

if not exist bin mkdir bin

echo Compiling Java source files...
javac -cp "lib/*" -d bin src/com/oasis/reservation/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful. Launching application...
    java -cp "bin;lib/*" com.oasis.reservation.ReservationApp
) else (
    echo Compilation failed! Please check errors above.
    pause
)

