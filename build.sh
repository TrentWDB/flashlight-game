#!/bin/bash
find src -name "*.java" > sources.txt
mkdir -p bin
javac -cp libs/gson-2.6.2.jar:libs/jbox2d/src -d bin @sources.txt
rm sources.txt

