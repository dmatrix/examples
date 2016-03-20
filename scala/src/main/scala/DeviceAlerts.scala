package main.scala

/**
  * Created by jules on 3/19/16.
  */
object DeviceAlerts {


  def sendTwilio(message: String): Unit = {
    //fill as necessary
    println("Twilio:" + message)
  }

  def sendSNMP(message: String): Unit = {
    //fill as necessary
    println("SNMP:" + message)
  }

  def sendPOST(message: String): Unit = {
    //fill as necessary
    println("HTTP POST:" + message)
  }

  def publishOnConcluent(message:String): Unit = {
    //fill as necessary
    println("Kafka Topic 'DeviceAlerts':" + message)
  }

  def publishOnPubNub(message: String): Unit = {
    //fill as necessary
    println("PubNub Channel 'DeviceAlerts':" + message)
  }

  def main(args: Array[String]): Unit = {

    for (arg <- args) {
        arg match {
          case "twilio" => sendTwilio("device 1 down!")
          case "snmp" => sendSNMP("device 2 down!")
          case "post" => sendPOST("device 3 down!")
          case "kafka" => publishOnConcluent("device 4 down!")
          case "pubnub" => publishOnPubNub("device 5 down")
        }
    }
  }
}
