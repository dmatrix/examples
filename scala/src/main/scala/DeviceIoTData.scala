package main.scala

/**
  * Created by jules on 3/16/16.
  * Case class used for Datasets in the Scala DBC Notebook
  */
case class DeviceIoTData (device_id: Long, device_name: String, ip: String, cca2: String, cca3: String, cn: String, latitude: Double, longitude: Double, scale:String, temp: Long, humidity: Long, battery_level: Long, c02_level: Long, lcd: String, timestamp: Long)

