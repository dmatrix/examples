#!/usr/bin/env python
from pubnub import Pubnub
from influxdb import InfluxDBClient
import sys
import signal
import os
import json
import argparse

"""
This short example illustrates the simplicity of using PubNub as a Publish-Subscribe cloud service. 
As an example, this program simulates as though multiple devices are registering themselves or announcing their
availability by publishing a channel. It subscribes to that publishing channels and receives its JSON object

Also, as an extension it can write to a socket where a Spark Streaming context is connected to process
live data streams of JSON objects from each device. Alternatively, it can write to a file in the directory
where a Spark streaming context is monitoring any data files.

Each JSON object received on its subscribed channles is the following format.

Each JSON object has the 
followin format:
 {"device_id": 97, 
  "timestamp", 1447886791
  "lat": 22, 
  "long": 82, 
  "scale: 
  "Celsius", 
  "temp": 22, 
  "device_name": "sensor-mac-word",
  "humidity": 15,
  "zipcode:" 95498
 }
author: Jules S. Damji 
"""

#
# global variables for InfluxDB
#
user = 'pubnub'
password = 'pubnub'
dbname = 'pubnub_devices'
dbuser = 'jules'
dbuser_password = 'influxdb'
client = None
#
#
# define some callbacks
#
def receive(message, channel):
  insert_into_dbs(["InfluxDB"], message)
	
#
# Integrate influx db insertion here as timeseries. Create a measurement point with tags and fields
#
def create_influxdb_point(jdoc, measurement):

  #use this incoming arg as measurement (or synonmous to table in DB)
  data={}
  data['measurement'] = measurement
  #
  # Define key tags in as part of this measurment table. Keys are going to be indexed so queries as
  # faster for its values
  tags = {}
  tags['humidity']= str(jdoc['humidity'])
  tags['temp'] = str(jdoc['temp'])
  tags['device_id'] = str(jdoc['device_id'])
  tags['device_name'] = str(jdoc['device_name'])
  tags['zipcode'] = str(jdoc['zipcode'])

  data['tags'] = tags
  data['time'] = jdoc['timestamp']
  #
  # define fields, which are not indexed
  #
  fields={}
  fields['lat'] = jdoc['lat']
  fields['long'] = jdoc['long']
  fields['scale'] = jdoc['scale']

  data['fields'] = fields

  return [data]


def insert_into_dbs(dbs, jdoc):

  ##TODO Convert this into a Singleton class so that we don't call this each time a message is recieved.
  client = InfluxDBClient("localhost", 8086, 'jules', 'influxdb', 'pubnub_devices')
  print ("Recieved JSON for insertion in DB: %s %s" % (dbs, json.dumps(jdoc, sort_keys="True")))
  for measurement in ["temperature", "humidity"]:
    influxDoc = create_influxdb_point(jdoc, measurement)
    print (influxDoc)
    client.write_points(influxDoc)

def on_error(message):
	print ("ERROR: " + str(message))

def signal_handler(signal, frame):
  println("Caugth Signal CNTL^C..exiting gracefully")
  sys.exit(0)

def parse_args():
    parser = argparse.ArgumentParser(description='PubNub subscriber for JSON messages for a public channel "devices"')
    parser.add_argument('--channel', type=str, required=True, default='devices',
                        help='PubNub public channel')
    parser.add_argument('--host', type=str, required=True, default='localhost',
                        help='hostname of InfluxDB http API')
    parser.add_argument('--port', type=int, required=True, default=8086,
                        help='port of InfluxDB http API')
    return parser.parse_args()

def main(channel="devices", host="localhost", port=8086):
  #
  #initialize the PubNub handle
  #
  pub_key = os.environ['PUB_KEY']
  sub_key = os.environ['SUB_KEY']
  
  pubnub = Pubnub(publish_key=pub_key, subscribe_key=sub_key)
  signal.signal(signal.SIGINT, signal_handler)
  
	# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
	# channel
	#
  print("Subscribing from PubNub Channel '%s'" % (channel))
  pubnub.subscribe(channels=channel, callback=receive, error=on_error)
  pubnub.start()

if __name__ == "__main__":
  args = parse_args()
  main(channel=args.channel)
