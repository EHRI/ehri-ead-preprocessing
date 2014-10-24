add_ehri_ids_to_ead_nodes
=========================

Add a path to each node of a EAD document


**Installation**

Clone the app in a directory

```
  $ git clone https://github.com/EHRI/ehri-ead-preprocessing.git
```


Build the application (maven 3.1 or higher version needed)
```
  $ cd ehri-ead-preprocessing/add_ehri_ids_to_ead_nodes
  $ mvn compile assembly:single
```


You will find the jar file **add_ehri_ids_to_ead_nodes-0.9-jar-with-dependencies.jar** in the 
*target* directory


**Use**

```
java -jar add_ehri_ids_to_ead_nodes-0.9-jar-with-dependencies.jar eadfile.xml
```

As output you will get a file called *eadfile_ehriID.xml*




