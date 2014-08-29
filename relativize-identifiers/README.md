relativize-identifiers
=========================

Part of the ehri-ead-preprocessing tools to normalise EAD files before importing into the EHRI database.

precondition: The EAD file has absolute identifiers, where unitids in each c-level include the full ID of their parent unitid
postcondition: The EAD file has relative identifiers.

usage:
java -jar relativize-identifiers/target/relativize-identifier-1.0-SNAPSHOT-jar-with-dependencies.jar <ead.xml>
