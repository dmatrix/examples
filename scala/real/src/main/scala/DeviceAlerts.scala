package main.scala

/**
  * Created by jules on 3/19/16.
  * Singleton helper for device alerts.
  */
object DeviceAlerts {


  def sendTwilio(message: String): Unit = {
    //TODO: fill as necessary
    println("Twilio:" + message)
  }

  def sendSNMP(message: String): Unit = {
    //TODO: fill as necessary
    println("SNMP:" + message)
  }

  def sendPOST(message: String): Unit = {
    //TODO: fill as necessary
    println("HTTP POST:" + message)
  }

  def publishOnConcluent(message:String): Unit = {
    //TODO: fill as necessary
    println("Kafka Topic 'DeviceAlerts':" + message)
  }

  def publishOnPubNub(message: String): Unit = {
    //TODO: fill as necessary
    println("PubNub Channel 'DeviceAlerts':" + message)
  }

  def main(args: Array[String]): Unit = {

    for (arg <- args) {
        val func = arg match {
          case "twilio" => sendTwilio _
          case "snmp" => sendSNMP _
          case "post" => sendPOST _
          case "kafka" => publishOnConcluent _
          case "pubnub" => publishOnPubNub _
        }
        func("Device 42 down!")
    }
  }
}
