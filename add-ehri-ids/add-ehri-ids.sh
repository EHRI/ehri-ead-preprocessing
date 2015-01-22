#!/bin/bash

# usage: ./add-ehri-ids.sh <input file folder/> <output file folder/>

if [[ ! -d "$2" ]]; then mkdir "$2"; fi

for FILE in `ls $1`; do
    echo "Adding an EHRI internal ID to each node in $FILE..."
    java -jar "target/add_ehri_ids_to_ead_nodes-0.9-jar-with-dependencies.jar" "$1$FILE"
    mv "$1"*_ehriID.xml "$2"
done


