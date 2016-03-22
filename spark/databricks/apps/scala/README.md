### Building a small Spark IoT application

In this directory there are two simple sample Spark applications that process and analyze JSON device information. The device data generated are purely
fictitious and simulated. Nonetheless, it's a good way to learn two important Spark features: DataFrames (introduced in 1.3) and 
Datasets (previewed in 1.6).

Though, Datasets are still experimental but will be finalized by Spark 2.0, you can still get a feel for its underlying APIs, which are no different than 
transformations and actions associated with RDDs.

### Using DataFrames.
To see how you can use relational operators and expression with DataFrame's API, this small sample code is a good introduction. Follow the steps below to build
it and subsequently run it on your local machine with Spark in local mode. It presume you have Spark 1.6 installed locally on your Mac.

* `sbt clean package`
* `spark-submit --class main.scala.iot.IoTDeviceDFApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>`

### Using Datasets.
Similarly, you can build and run this Dataset version of the Spark App.

* `sbt clean package`
* `spark-submit --class main.scala.iot.IoTDeviceDSApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>`

### Generating JSON Data 

* `cd to ../../../../scala/`
* `sbt clean package`
* `scala -cp target/scala-2.10/src-main-scala_2.10-1.0.jar main.scala.GenerateIoTDeviceData 1200 <output_path/devices.json>`

The first argument to this Scala app is a number of devices that **must** be a multiple of three, and the second is path to a JSON output file.
You can generated millions of devices with this app. Using Executor Service thread pool and a Latch Countdown by assigning batches, this short
program is farily efficient.

(I have generated over 9M entries!)

For example a run may look as follows:

`scala -cp target/scala-2.10/src-main-scala_2.10-1.0.jar main.scala.GenerateIoTDeviceData 1200 /Users/jules/data/iot/devices.json`

    Generating 1200 Devices' data in /Users/jules/data/iot/devices.json
    Launching 3 threads and waiting for them to finish via Latch Countdown mechanism...
    Generating Devices info in pool-1-thread-1
    Generating Devices info in pool-1-thread-2
    Generating Devices info in pool-1-thread-3
    Batch of 400 devices completed...
    Batch of 400 devices completed...
    Batch of 400 devices completed...

    All Device Generators Threads ended.
