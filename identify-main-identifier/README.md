identify_main_identifier
========================

Identify the main identifier used in an EAD document.
It creates the new tag 
```
<unitid label="ehri_main_identifier">XXXXXX<unitid>
```


**Installation**

Clone the app in a directory

```
  $ git clone https://github.com/KepaJRodriguez/identify_main_identifier
```


Build the application (maven 3.1 or higher version needed)
```
  $ cd identify_main_identifier
  $ mvn compile assembly:single
```


You will find the jar file **identify_main_identifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar** in the 
*target* directory


**Use**

```
java -jar identify_main_identifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar eadfile.xml
```

As output you will get a file called *eadfile_ehriID.xml*
