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

"""
This short example illustrates the simplicity of using PubNub Realtime Streaming Netowrk,
and how to use PubNub SDK to program the netowrk, to publish data streams and to subscribe data streams.

Though the example is simple, it simulates as though multiple devices are registering themselves or announcing their
availability by publishing their state on a dedicated channel. In reality this could be a deployment of meters or sensors
in an area code that you wish to monitor for tial and do some realtime analysis using Spark.

Also, as an optional extension, the app can write to a socket or a directory where a Spark Streaming context monitoring for live
live data streams of JSON objects from each device. For directory, the Spark application must run on the same JVM as this app and in local mode.

I employ a thread that simulates mulitple devices acting as publishers, but in reality each JSON data object could be published
separately by each device using PubNub's publish-subscribe API. 

It can either downloads a list of words from the Internet (http://www.textfixer.com/resources/common-english-words.txt) or create the size specifed on the commandline options
. From the list or bath, it composes device names. Each JSON object has the 
followin format:
 {"device_id": 97, 
  "timestamp", 1447886791.607918,
  "lat": 22, 
  "long": 82, 
  "scale: 
  "Celius", 
  "temp": 22, 
  "device_name": "sensor-mac-word",
  "humidity": 15,
  "zipcode:" 95498
 }

 To run this program to create json files into the destinattion directory for Spark Streaming consumption:
 $ python pub_dev_words.py {-u http://www.textfixer.com/resources/common-english-words.txt | -b num_of_devices} -c devices -i 1 -d data
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
  id=0
  for b in batches:
    for w in b:
      id=id+1
      device_msg = create_json(id, w)
      write_to_dir(filed, device_msg)
      pubnub.publish(channel=ch, message=device_msg, error=on_error)

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
  for i in range(1, size):
    batches.append(str(i))
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
#
# create a json object with attributes and values
#
def create_json(id, d):
  temp = random.randrange(0, 35)
  (x, y) = random.randrange(0, 100), random.randrange(0, 100)
  time.sleep(0.025)
  ts = time.time()
  if id % 2 == 0:
    d = "sensor-pad-" + d + get_random_word()
  elif id % 3 == 0:
    d = "device-mac-" + d + get_random_word()
  elif id % 5 == 0:
    d = "therm-stick-" + d + get_random_word()
  else:
    d = "meter-gauge-" + d + get_random_word()
    
  zipcode = random.randrange(94538,97107)
  humidity = random.randrange(25, 100)
  return json.dumps({'device_id': id, 'device_name': d, 'timestamp': ts, 'temp': temp, 'scale': 'Celius', "lat": x, "long": y, 'zipcode': zipcode, 'humidity': humidity}, sort_keys=True)

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
#
# the main program
#
if __name__ == "__main__":
  url, ch, num_of_devices, data_dir = None, None, None, None
  iterations = 3
  #
  # parse command line arguments
  #
  try:
    opts, args = getopt.getopt(sys.argv[1:],"u:n:c:i:d:",["url=","ndevices=", "channel=","iterations=","dir="])
  except getopt.GetoptError:
      print("Usage: pub_dev_words.py {-u url | -n num_of_devices} -c channel -i iterations -d dirname [--spark=yes] [--host <hostname> --port portno]")
      sys.exit(-1)

  for opt, arg in opts:
    if opt in ("-u", "url="):
       url = arg
    elif opt in ("-i", "iterations="):
      iterations = arg
    elif opt in ("-c", "channel="):
       ch = arg
    elif opt in ("-n", "ndevices="):
        num_of_devices = int(arg)
    elif opt in ("-d", "dir="):
       data_dir = arg

  if url and num_of_devices:
    print "(Can't specify both url and number of devices. Choose one."
    print("Usage: pub_dev_words.py {-u url | -n num_of_devices} -c channel -i iterations -d dirname [--spark=yes] [--host <hostname> --port portno]")
    sys.exit(-1)

  #
  #Initialize the PubNub handle, with your personal keys
  #
  pub_key=os.environ['PUB_KEY']
  sub_key=os.environ['SUB_KEY']
  pubnub = Pubnub(publish_key=pub_key, subscribe_key=sub_key)
  #
  # fetch the batches
  #
  if url:
    batches = get_batches(url)
  else:
    batches = get_large_batches(num_of_devices)

  if batches == None or len(batches) == 0:
    print >> sys.stderr, "URL: " + url + "does not contain any words. Use the -n <num_of_devices option"
    sys.exit(-1)
  #
  #Create psuedo devices for provisioning as though they are all publishing upon activation
  #Use number of iterations and sleep between them. For each iteration, launch a thread that will
  #execute the function.
  #
  for i in range(int(iterations)):
    filed, file_name = get_file_handle(data_dir, i)
    start_new_thread(publish_devices_info, (ch,filed))
    time.sleep(30)
    print ("Devices' info published on PubNub Channel '%s' and data written to file '%s'" % (ch, os.path.join(data_dir, file_name)))
    close_file_handle(filed)

