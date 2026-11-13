#!/bin/bash
# Script to compile and run OASIS INFOBYTE Task 1 - Online Reservation System

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

echo "=========================================================="
echo "  OASIS INFOBYTE - Task 1: Online Reservation System"
echo "=========================================================="

mkdir -p bin

echo "Compiling Java source files..."
javac -cp "lib/*" -d bin src/com/oasis/reservation/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful. Launching application..."
    java -cp "bin:lib/*" com.oasis.reservation.ReservationApp
else
    echo "Compilation failed! Please check errors above."
    exit 1
fi

