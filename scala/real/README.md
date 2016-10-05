### Building a Spark IoT Application.

As directory containing simple Scala singleton and utility classes, I use the library in my Databricks Scala notebooks. Additionally, I use a collection
of classes and companion objects to generate IoT device data for the Databricks Community Edition Scala Notebooks.

To build the target library, simply type in:

`sbt clean package`

All dependencies stipulated in the *build.sbt* will be pulled from its appropriate repos into your local repo, and the resulting jar will be in 
*target/scala-2.10/src-main-scala_2.10-1.0.jar* directory. 
 

### How to generate trending IoT Data

All you need is to build the library, as show above an then issue the following command, along with the desired command line arguments. 

1. `sbt clean package`
2. `scala -cp target/scala-2.10/src-main-scala_2.10-1.0.jar main.scala.GenerateStreamingIoTDeviceData <git_dir>/py/ips/data/country_codes.txt <git_dir>py/ips/data/ips_info.txt <output_directory> <number_of_files> <number_of_device_entries_per_file> <trend_every_nth_file>`

###  Command Line Arguments
_output_directory_ is the destination where these files will be created. Each file will be labelled as _device_N.json_ 

_number_of_files_ is the number of files to be created, normally anywhere from 100-200 files is sufficient for a large dataset

_number_of_devices_per_file_ is the total number of devices JSON entry per file. While themaxium is maximum is 198164 (the number of uniquie IP addresses in the ips_info.txt), 
for community edition you want to keep this number small, perhaps 250-300 devices

_trend_every_nth_file_ is the number you want some trending for sensor readings such as temperature, signal, and c02_levels trend, an in upward fashion.

The timestamp for each device entry is in seconds, and each device entry is 2 seconds apart. A typical device entry looks as follows:

`{"device_id": 0, "device_type": "sensor-ipad", "ip": "68.161.225.1", "cca3": "USA", "cn": "United States", "temp": 25, "signal": 23, "battery_level": 8, "c02_level": 917, "timestamp" :1475600496 }`

`{"device_id": 1, "device_type": "sensor-igauge", "ip": "213.161.254.1", "cca3": "NOR", "cn": "Norway", "temp": 30, "signal": 18, "battery_level": 6, "c02_level": 1413, "timestamp" :1475600498 }`



PR requests are welcome!

Have Fun. 