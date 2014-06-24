#!/bin/bash
mkdir chi-specific-preprocessed
for fl in *.xml; do
  sed "s/\`/\'/g" $fl > chi-specific-preprocessed/$fl
done
echo "now go and add the revisiondesc"
