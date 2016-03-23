package main.scala.iot

/**
  * Created by jules on 3/22/16
  *
  */
//case class for the Device data
//"device_id": 12000, "device_name": "sensor-pad-12000gtYLp00o", "timestamp":1454965623, "temp": 20, "scale": "Celsius", "latitude": 51, "longitude": 51, "zipcode": 94844, "humidity": 64}
case class DeviceData (device_id: Long, device_name: String, timestamp: Long, temp: Long, scale:String, latitude: Long, longitude: Long, zipcode: Long, humidity: Long)
