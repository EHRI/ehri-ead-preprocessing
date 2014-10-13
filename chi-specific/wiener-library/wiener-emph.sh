#!/bin/bash

# usage: ./wiener-emph.sh <saxon.jar> <input file folder/>

for FILE in `ls $2`; do
    echo "Transforming $FILE..."
    java -jar "$1" -xsl:wiener-emph.xsl -s:"$2/$FILE" -o:"$2-emph-removed/$FILE"
done
