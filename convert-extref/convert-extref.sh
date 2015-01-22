#!/bin/bash

# usage: ./convert-extref.sh <saxon.jar> <input file folder/>

for FILE in `ls $2`; do
    echo "Transforming extref in $FILE..."
    java -jar "$1" -xsl:convert-extref.xsl -s:"$2/$FILE" -o:"$2-extref-converted/$FILE"
done
