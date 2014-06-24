#!/bin/bash
EADPREPROCESSDIR=/Users/linda/git/ehri-ead-preprocessing
WPATH=wpath
EHRIID=ehriID
MAINID=mainids
PREPROCESSED=preprocessed
NOW=$(date +%d-%m-%Y)
GITHASH=$1

if [ -d $WPATH ]; then
  rm -R $WPATH
fi
mkdir wpath
for fl in ./chi-specific-preprocessed/*.xml; do
  java -jar $EADPREPROCESSDIR/add-path-structure/target/add_paths_to_ead_nodes-0.0.1-SNAPSHOT-jar-with-dependencies.jar $fl
done
mv chi-specific-preprocessed/*wpath.xml $WPATH
echo "path structure added, dir: $WPATH"

if [ -d $EHRIID ]; then
  rm -R $EHRIID
fi
mkdir $EHRIID
for fl in ./$WPATH/*.xml; do
  java -jar $EADPREPROCESSDIR/add-ehri-ids/target/add_ehri_ids_to_ead_nodes-0.0.1-SNAPSHOT-jar-with-dependencies.jar $fl
done
mv wpath/*ehriID.xml $EHRIID
echo "ehri-internal-identifier added, dir: $EHRIID"

if [ -d $MAINID ]; then
  rm -R $MAINID
fi
mkdir $MAINID
for fl in ./$EHRIID/*.xml; do
  java -jar $EADPREPROCESSDIR/identify-main-identifier/target/identify_main_identifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar $fl
done
mv ehriID/*mainids.xml $MAINID
echo "main-identifier identified, dir: $MAINID"

if [ -d $PREPROCESSED ]; then
  rm -R $PREPROCESSED
fi
mkdir $PREPROCESSED
cd $MAINID
for fl in *.xml; do
  sed "s/<item>EHRI added a unitid with label \"ehri_main_identifier\" to indicate the identifier provided by the institution that EHRI will use as the identifier of the unit.<\/item>/<item>EHRI added a unitid with label "ehri_main_identifier" to indicate the identifier provided by the institution that EHRI will use as the identifier of the unit.<\/item><\/change><change><date>$NOW<\/date><item>Deze toegang is voorbewerkt door EHRI met behulp van https:\/\/github.com\/EHRI\/ehri-ead-preprocessing\/commit\/$GITHASH<\/item>/g" $fl > ../$PREPROCESSED/$fl
done
echo "all preprocessing done, dir: $PREPROCESSED"
