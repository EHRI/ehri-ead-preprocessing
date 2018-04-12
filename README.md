[![Build Status](https://travis-ci.org/EHRI/ehri-ead-preprocessing.svg?branch=master)](https://travis-ci.org/EHRI/ehri-ead-preprocessing)

Bundesarchiv
============

Author
------

Dirk Roorda (DANS) dirk.roorda@dans.knaw.nl

Description of the transformation
---------------------------------

This transformation detects `<did>` elements without a proper `<unitid>` element
inside. Only `<did>` elements that are children of a `<c>` element are
considered.

In particular, the `<did>` directly below `<archdesc>` is not taken into
account.

A proper <unitid> element has an attribute `type` with value `call number`.

If such a `<unitid>` is missing, a fresh one will be generated and inserted as
first element in the parent `<did>`.

The new `<unitid>` will be filled as follows:

*   text content: the value of the `id` attribute of the parent `<c>` element.
*   attribute `label` = `ehri_main_identifier`
*   attribute `encodinganalog` = `3.1.1`

The attributes `label` and `encodinganalog` are filled with fixed values!

Usage
-----

```sh
python3 {scriptPath}/moveId.py {prePath} {postPath}
```

Here

*   {scriptPath} is the path leading to the place where `moveId.py` exists on the
    server;
*   {prePath} is the full path name of the incoming EAD file;
*   {postPath} is the full path name of the outgoing EAD file.

Details
-------

Python3 is installed on the staging server as part of the standard Ubuntu 16.xx
LTS distribution.

It is assumed that the input EAD exists as a file on disk. The output will also
be generated as a file on disk.

It is assumed that the calling process knows what to use for {scriptPath},
{prePath} and {postPath}.
