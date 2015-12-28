object TestIotDevice {
  def main(args: Array[String]) {
    val nDevices = args(0).toInt
    val batches = DeviceProvision.getDeviceBatch(nDevices)
    batches.foreach(println(_))
  }
}