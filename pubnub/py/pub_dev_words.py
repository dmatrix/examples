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
This short example illustrates the simplicity of using PubNub Realtime Streaming Netowrk. 

As an example, it simulates as though multiple devices are registering themselves or announcing their
availability by publishing on a channel.  

Also, as an optional extension, it can write to a socket or a directory where a Spark Streaming context monitoring for live
live data streams of JSON objects from each device. For directory, the Spark application must run on the same JVM as this app—and 
in local mode.

Here I similuate mulitple devices publishing by using a thread, but in reality each JSON data object could be published
separately by each device using PubNub's publish-subscribe API. 

It downloads a list of words from the Internet (http://www.textfixer.com/resources/common-english-words.txt) and uses them as device names. Each JSON object has the 
followin format:
 {"device_id": 97, 
  "timestamp", 1447886791.607918,
  "lat": 22, 
  "long": 82, 
  "scale\: 
  "Celius", 
  "temp": 22, 
  "device_name": "sensor-mac-word"
 }
author: Jules S. Damji 
"""
#
#global variables, need that for thread functions
#
pubnub = None
batches = []
device_file = "devices.json"

def on_error(message):
  print ("ERROR: Publish " + str(message))
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
      pubnub.publish(ch, device_msg, error=on_error)
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
  ts = time.time()
  d = "sensor-mac-" + d
  return json.dumps({'device_id': id, 'device_name': d, 'timestamp': ts, 'temp': temp, 'scale': 'Celius', "lat": x, "long": y}, sort_keys=True)

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
def get_file_handle(dir):
  global device_file
  fd = None
  try:
    path = os.path.join(dir, device_file)
    fd = open(path, "w")
  except IOError as e:
    print "I/O error({0}): {1}".format(e.errno, e.strerror)
    sys.exit(-1)
  return fd

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
    print "I/O error({0}): {1}".format(e.errno, e.strerror)
#
# the main program
#
if __name__ == "__main__":
  url, ch , data_dir = None, None, None
  iterations = 3
  #
  # parse command line arguments
  #
  try:
    opts, args = getopt.getopt(sys.argv[1:],"u:c:i:d:",["url=","channel=","iterations=","dir="])
  except getopt.GetoptError:
      print("Usage: pub_dev_words.py -u url -c channel -i iterations -d dirname [--spark=yes] [--host <hostname> --port portno]")
      sys.exit(-1)

  for opt, arg in opts:
    if opt in ("-u", "url="):
       url = arg
    elif opt in ("-c", "channel="):
       ch = arg
    elif opt in ("-d", "dir="):
       data_dir = arg
  #
  #initialize the PubNub handle, with your personal keys
  #
  pubnub = Pubnub(publish_key="demo", subscribe_key="demo")
  #
  # fetch the batches
  #
  batches = get_batches(url)
  if batches == None or len(batches) == 0:
    print "URL: " + url + "does not contain any words. Using a different URL"
    sys.exit(-1)
  #
  #Create psuedo devices for provisioning as though they are all publishing upon activation
  #Use number of iterations and sleep between them. For each iteration, launch a thread that will
  #execute the function.
  #
  filed = get_file_handle(data_dir)
  for i in range(int(iterations)):
    start_new_thread(publish_devices_info, (ch,filed))
    time.sleep(5)
  close_file_handle(filed)
  
  print ("Devices' info published on PubNub Channel '%s' and data written to file '%s'" % (ch, os.path.join(data_dir, device_file)))
