declare namespace ead = "urn:isbn:1-931666-22-9";
declare default element namespace "urn:isbn:1-931666-22-9";

(: Merge two controlaccess elements (source goes into target)
 including duplicates :)
declare function local:mergeCA ($caSource, $caTarget)  {
  copy $c := $caTarget
  modify (
    for $term in $caSource/*
    let $ttype := $term/name()
    let $tatts := $term/attribute::*
    let $tcont := $term/text()
    where not(exists($c/*[name() = $ttype][text() = $tcont]))
    return insert node $term into $c
  )
  return $c

};

(: Merge controlaccess elements from sourceDoc in corresponding units in the
   targetDoc, matching the units by their unitids :)
declare function local:mergeCADoc ($sourceDoc, $targetDoc) {
  copy $td := $targetDoc
  modify (
    for $sourceUnit in $sourceDoc//*[starts-with(name(.),"c0")][controlaccess]
    let $sourceUnitid := $sourceUnit/did/unitid
    let $sourceCA := $sourceUnit/controlaccess
    let $targetUnit := $td//unitid[string(.) = $sourceUnitid]/parent::did/parent::*
    let $targetUnitid := $targetUnit/did/unitid
    let $targetCA := $targetUnit/controlaccess
    for $term in $sourceCA/*
    let $ttype := $term/name()
    let $tatts := $term/attribute::*
    let $tcont := $term/normalize-space()
    where not(exists($targetCA/*[name() = $ttype][text() = $tcont]))
      or $term/attribute::*
    return insert node $term into $targetCA

  )
  return $td
};

let $wp2 := doc("YadVashem/tm_ead_yv.xml")
let $o64 := doc("YadVashem/MS1_O64_ENG.xml")

return local:mergeCADoc($wp2, $o64)
