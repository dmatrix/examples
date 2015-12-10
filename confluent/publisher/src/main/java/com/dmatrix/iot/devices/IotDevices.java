package com.dmatrix.iot.devices;
import java.util.concurrent.ThreadLocalRandom;

public class IotDevices {
    /**
     * @return temperature within acceptable range
     */
    public static int getTemp() {
        return ThreadLocalRandom.current().nextInt(5, 54 + 1);
    }
    /**
     * @return X or Y coordinate for the device in question
     */
    public static int getCoordinate() {
        return ThreadLocalRandom.current().nextInt(1, 99 + 1);
    }

    /**
     * @param id device_id
     * @return return zipcode based on id
     */
    public static int getZipCode(int id) {
        if (id % 2 == 0) {
            return 94538;
        } else if (id % 3 == 0) {
            return 96545;
        } else if (id % 5 == 0) {
            return 97100;
        } else {
            return 97108;
        }
    }
    /**
     * Get device type based on the Id
     *
     * @param id
     * @return
     */
    public static String getDeviceType(int id) {
        if (id % 2 == 0) {
            return "sensor-" + id + "25ForJSD";
        } else if (id % 3 == 0) {
            return "device-mac-" + id + "25ForJSD";
        } else if (id % 5 == 0) {
            return "therm-stick-" + id + "25ForJSD";
        } else {
            return "meter-gauge-" + id + "25ForJSD";
        }
    }
    /**
     * @return acceptable humidity for the device
     */
    public static int getHumidity() {
        return ThreadLocalRandom.current().nextInt(35, 99 + 1);
    }
}