#!/bin/bash

# usage: ./convert-emph.sh <saxon.jar> <input file folder/>

for FILE in `ls $2`; do
    echo "Transforming $FILE..."
    java -jar "$1" -xsl:convert-emph.xsl -s:"$2$FILE" -o:"$2/../ead-emph-converted/$FILE"
done
