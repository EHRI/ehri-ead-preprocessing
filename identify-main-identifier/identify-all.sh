#!/bin/bash

# usage: ./identify-all.sh <input folder>

for FILE in `ls $1`; do
    java -jar target/identify_main_identifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar "$1$FILE" UNITID
done
