package main.scala.iot

import org.apache.spark.unsafe.types.UTF8String;


/**
  * Created by jules on 2/6/16.
  *
  * case class for the DeviceInfo Message
  * {"device_id": 0,
  * "device_name": "sensor-025ForJSD",
  * "ip": "192.34.5.0",
  * "temp": 35,
  * "humidity": 82,
  * "lat": 33,
  * "long": 8,
  * "zipcode": 94538,
  * "scale": "Celsius",
  * "timestamp": 1454785287191
  * }
  */
case class IOTDevice (deviceId: Int, devcieName: UTF8String, ip: UTF8String, temp: Int, humidity: Int, lat: Int, long: Int, zipCode: Int, scale: UTF8String, timestamp: Long) {

}
