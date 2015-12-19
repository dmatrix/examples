TO USE THE JAVA CONSUMER

1. cd examples/confluent/subscriber
2. mvn clean package
3. mvn exec:java -Dexec.mainClass="com.dmatrix.iot.devices.SubscribeIoTDevices" -Dexec.args="localhost:2181 group devices 1 http://localhost:8081"
