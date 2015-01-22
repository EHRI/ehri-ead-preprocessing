#!/bin/bash

# usage: ./add-path-structure.sh <input file folder/> <output file folder/>

if [[ ! -d "$2" ]]; then mkdir "$2"; fi

for FILE in `ls $1`; do
    echo "Adding an EHRI internal ID to each node in $FILE..."
    java -jar target/add_paths_to_ead_nodes-0.0.1-SNAPSHOT-jar-with-dependencies.jar "$1$FILE"
    mv "$1"*_wpath.xml "$2"
done





