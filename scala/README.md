### Building a target Scala jar
A directory with simple Scala singleton and utility classes, I use the library in my Databricks Scala notebooks. Additionally, I use a collection
of classes and companion objects to generate IoT device data for the Databricks Community Edition Scala Notebooks.

To build the target library, simply type in:

`sbt clean package`

All dependencies stipulated in the *build.sbt* will be pulled from its appropriate repos into your local repo, and the resulting jar will be in 
*target/scala-2.10/src-main-scala_2.10-1.0.jar* directory. 
 
Have Fun.