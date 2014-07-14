leave-only-one-identifier
=========================

Part of the ehri-ead-preprocessing tools to normalise EAD files before importing into the EHRI database.

precondition: the eadfile has exactly one unitid with label="ehri_internal_id" per did and zero or more unitids with label="ehri_main_identifier"
postcondition: the resulting file will have exactly one unitid with label="ehri_main_identifier". if there were multiple ehri_main_identifiers before, their label is renamed to "ehri_multiple_identifier".

usage:
java -jar leave-only-one-identifier/target/leave-only-one-identifier-1.0-SNAPSHOT-jar-with-dependencies.jar <ead.xml>
