/**
 * Singleton class for InfluxDB
 *
 * author:Jules S. Damji
 */
package com.dmatrix.iot.devices;

import org.influxdb.*;
import org.influxdb.dto.Point;

public final class InfluxDBConnection {

    private static InfluxDBConnection instance = null;
    private static InfluxDB influxDB = null;
    private String user = "jules";
    private String pwd  = "influxdb";
    private static String db   = "pubnub_devices";

    /**
     *  private constructor so no other class or thread can instantiate it
     */
    private InfluxDBConnection() throws Exception {
       try {
           influxDB = InfluxDBFactory.connect("http://localhost:8086", user, pwd);
       } catch (Exception ex) {
           ex.printStackTrace();
           throw new Exception(String.format("Failed to Connect to InfluxDB on the localhost:8086 with user='%s'/password='%s'", user, "******"));
       }
    }
    /**
     *
     * @return the single instance to InfluxDB
     */
    public static InfluxDBConnection getInstance() throws Exception {
        if(instance == null) {
            instance = new InfluxDBConnection();
        }
        return instance;
    }

    public static void writePoint(Point pt) {
        influxDB.write(db, "default", pt);
    }

}
