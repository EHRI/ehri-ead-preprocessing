#!/bin/bash

# usage: ./identify-all.sh <input folder> <output folder>

if [[ ! -d "$2" ]]; then mkdir "$2"; fi

for FILE in `ls $1`; do
    java -jar target/identify_main_identifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar "$1/$FILE" UNITID
    mv "$1"/*mainids.xml "$2"
done
