#!/bin/bash

# Prepare Wiener Library data for import
# Usage: ./prepare-data.sh <saxon.jar> <input folder>
# Final output should appear in ${input folder}-mid-emph-removed-emph-converted

# Add EHRI IDs
cd add-ehri-ids
./add-ehri-ids.sh "$2/" "$2-eids/"
cd ..

# Add path structure to nodes
cd add-path-structure
./add-path-structure.sh "$2-eids/" "$2-eids-wpath/"
cd ..

# Identify main identifier (which is UNITID for Wiener Library)
cd identify-main-identifier
./identify-all.sh "$2-eids-wpath" "$2-mid"
cd ..

# Replace <emph> in controlled access points
cd chi-specific/wiener-library
./wiener-emph.sh "$1" "$2-mid"
cd ../..

# Convert <emph> in <p> to Markdown formatting
cd convert-emph
./convert-emph.sh "$1" "$2-mid-emph-removed"
cd ..

# Convert <extref> to Markdown links
cd convert-extref
./convert-extref.sh "$1" "$2-mid-emph-removed-emph-converted"
cd ..
