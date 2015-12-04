#!/usr/bin/env python
from pubnub import Pubnub
from thread import start_new_thread
import getopt
import urllib2
import time
import sys
import socket
import json
import random
import os
import argparse

"""
This short example illustrates the simplicity of using PubNub Realtime Streaming Netowrk,
and how to use PubNub SDK to program the netowrk, to publish data streams and to subscribe data streams.

Though the example is simple, it simulates as though multiple devices are registering themselves or announcing their
availability by publishing their state on a dedicated channel. In reality this could be a deployment of meters or sensors
in an area code that you wish to monitor for trial and do some realtime analysis using Spark.

Also, as an optional extension, the app can write to a socket or a directory where a Spark Streaming context is monitoring for live
data streams of JSON objects from each device. For directory streaming, the Spark application must run on the same JVM as this app and in local mode.

I employ a thread that simulates mulitple devices acting as publishers, but in reality each JSON data object could be published
separately by each device using PubNub's publish/subscribe API. 

It creates the size or number of devices specifed on the commandline options

From the number of devices it composes device names. Each JSON object has the 
following format:

 {"device_id": 97,
  "ip": "191.35.83.75",
  "timestamp", 1447886791.607918,
  "lat": 22, 
  "long": 82, 
  "scale: 
  "Celius", 
  "temp": 22, 
  "device_name": "sensor-mac-id<random_string>",
  "humidity": 15,
  "zipcode:" 95498
 }

 To run this program to create json files in the destination directory for Spark Streaming consumption:
 $ python publish_devices.py -b num_of_devices -c devices -i 1 -d data

author: Jules S. Damji 

"""
#
#global variables, need that for thread functions
#
pubnub = None
batches = []
device_file = "device.json"

def on_error(message):
  print >> sys.stderr, "ERROR: Publish " + str(message)
#
# Publish the data to the device channel as well as write to the data directory.
# If we are using Spark Streaming, then Spark Context can monitor this directory for a new file
# and create a json datastream for processing, if enabled
#
def publish_devices_info(ch, filed):
  global pubnub, batches
  for id in batches:
    device_msg = create_json(id)
    write_to_dir(filed, device_msg)
    pubnub.publish(channel=ch, message=device_msg, error=on_error)
    id=id + 1

#
# get random letters
#
def get_random_word():
  word = ''
  for i in range(8):
    word += random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789')
  return word

#
# instead of fething words from the URL, create 1..range(size) for the number of devices
#
def get_large_batches(size):
  batches = []
  for i in range(0, size):
    batches.append(i)
  return batches

#
#
# given a url fetch the words (or could be device names) in the url that are comma separated
#
def get_batches(url):
  webFile = urllib2.urlopen(url).read()
  words = webFile.split(",")
  #add an addtional word of the batches are not even
  if len(words) % 10 != 0:
    words = words + ['jules']
    j = 10
    for i in range(0, len(words), 10):
      batches.append(words[i:j])
      j = j + 10
    return batches

def get_ip_addr():
  n1 = random.randrange(60, 209)
  n2 = random.randrange(1, 127)
  n3 = random.randrange(1, 127)
  n4 = random.randrange(1,127)

  ip = str(n1) + "." + str(n2) + "." + str (n3) + "." + str(n4)
  return ip
#
# create a json object with attributes and values
#
def create_json(id):
  temp = random.randrange(0, 35)
  (x, y) = random.randrange(0, 100), random.randrange(0, 100)
  time.sleep(0.025)
  ts = time.time()
  ip = get_ip_addr()

  if id % 2 == 0:
    d = "sensor-pad-" + str(id) + get_random_word()
  elif id % 3 == 0:
    d = "device-mac-" + str(id) + get_random_word()
  elif id % 5 == 0:
    d = "therm-stick-" + str(id) + get_random_word()
  else:
    d = "meter-gauge-" + str(id) + get_random_word()
    
  zipcode = random.randrange(94538,97107)
  humidity = random.randrange(25, 100)
  ip = get_ip_addr()
  return json.dumps({"device_id": id, "device_name": d, "ip": ip, "timestamp": ts, "temp": temp, "scale": "Celsius", "lat": x, "long": y, "zipcode": zipcode, "humidity": humidity}, sort_keys=True)

#
# create a connection to a socket where a spark streaming context will listen for incoming JSON strings
#
def create_spark_connection(port, host):
  try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  except socket.error:
    print 'Failed to create socket. Error code: ' + str(msg[0]) + ' , Error message : ' + msg[1]
    return 0
  try:
    remote_ip = socket.gethostbyname(host)
  except socket.gaierror:
    print 'Hostname could not be resolved. Exiting'
    return 0

  s.connect((remote_ip, port))
  return s

#
# close the connection to the stream
#
def close_spark_connection(s):
  if s:
    s.close()
#
# send JSON object or message to the Spark streaming context listener
#
def send_to_spark(s, dmsg):
  s.sendall(device_msg)

#
# Get the file descriptor for the directory/filename
# Note that this file will overwrite existing file
#
def get_file_handle(dir, i):
  global device_file
  fd = None
  fname = str(i) + "-" + device_file
  try:
    path = os.path.join(dir, fname)
    fd = open(path, "w")
  except IOError as e:
    print >> sys.stderr, "I/O error({0}): {1}".format(e.errno, e.strerror)
    sys.exit(-1)
  return fd, fname

#
# close the filehandle
#
def close_file_handle(fd):
  fd.close()
#
# write the device json to the file
#
def write_to_dir(fd, djson):
  try:
    fd.write(djson)
    fd.write("\n")
  except IOError as e:
    print >> sys.stderr, "I/O error({0}): {1}".format(e.errno, e.strerror)

def parse_args():
    parser = argparse.ArgumentParser(description='PubNub Publisher for JSON devices messages for a public channel "devices"')
    parser.add_argument('--channel', type=str, required=True, default='devices',
                        help='PubNub public channel')
    parser.add_argument('--number', type=int, required=True, default=50,
                        help='Number of psuedo-devices generated')
    parser.add_argument('--iterations', type=int, required=True, default='3',
                        help='Number of iterations to run')
    parser.add_argument('--data_dir', type=str, required=True, default="data_dir",
                        help='Directory to write JSON data files for the Spark Streaming application')
    parser.add_argument('--host', type=str, required=False, default='localhost',
                        help='hostname of Cassandra API[TODO]')
    parser.add_argument('--port', type=int, required=False, default=9160,
                        help='port of Cassandra API[TODO]')
    return parser.parse_args()

if __name__ == "__main__":

  args = parse_args()
  ch=args.channel
  iterations=args.iterations
  number=args.number
  data_dir=args.data_dir
  #
  #Initialize the PubNub handle, with your personal keys
  #
  pub_key=os.environ['PUB_KEY']
  sub_key=os.environ['SUB_KEY']
  pubnub = Pubnub(publish_key=pub_key, subscribe_key=sub_key)
  #
  # fetch the batches
  batches = get_large_batches(number)
  #
  #Create psuedo devices for provisioning as though they are all publishing upon activation
  #Use number of iterations and sleep between them. For each iteration, launch a thread that will
  #execute the function.
  #
  for i in range(int(iterations)):
    print ("Creating iteration '%d' for batch of %d devices" % (i, number))
    filed, file_name = get_file_handle(data_dir, i)
    start_new_thread(publish_devices_info, (ch,filed))
    time.sleep(30)
    print ("%d Devices' infomation published on PubNub Channel '%s' and data written to file '%s'" % (number, ch, os.path.join(data_dir, file_name)))
    close_file_handle(filed)

