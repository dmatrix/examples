#!/usr/bin/env python
from pubnub import Pubnub
import urllib2
import time
import sys
import socket
import json
import random
"""
This short example illustrates the simplicity of using PubNub as a Publish-Subscribe cloud service. 
As an example, it simulates as though multiple devices are registering themselves or announcing their
availability by publishing a channel. 

Also, as an extension it can write to a socket where a Spark Streaming context is connected to process
live data streams of JSON objects from each device.

Here we sumiluate within a single example, but in reality ech JSON data object could be be published
separately from its embedded PubNub API. 

It downloads a list of words form the Internet and uses them as device names. Each JSON object has the 
followin format:
 {"device-id": 97, 
 "lat": 22, 
 "long": 82, 
 "scale\: 
 "Celius", 
 "temp": 22, 
 "device-name": "tis"
 }
author: Jules S. Damji 
"""

#
#
# given a url fetch the words in the url that are comma separated
#
def get_batches(url):
  webFile = urllib2.urlopen(url).read()
  words = webFile.split(",")
  #add an addtional word of the batches are not even
  if len(words) % 10 != 0:
    words = words + ['jules']
    j = 10
    batches = []
    for i in range(0, len(words), 10):
      batches.append(words[i:j])
      j = j + 10
    return batches
#
# create a json object with attributes and values
#
def create_json(id, d):
  temp = random.randrange(10, 35)
  (x, y) = random.randrange(0, 100), random.randrange(0, 100)
  return json.dumps({'device-id': id, 'device-name': d, 'temp': temp, 'scale': 'Celius', "lat": x, "long": y}, sort_keys=True)

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

if __name__ == "__main__":
  if len(sys.argv) != 3:
    print("Usage: pub_dev_words.py url channel [--spark=yes --host <hostname> --port portno]")
    sys.exit(-1)

  url, ch = sys.argv[1:]
  #
  #initialize the PubNub handle
  #
  pubnub = Pubnub(publish_key="pub-c-c3575cb5-36fc-4cff-b4e8-280262ed5f00", subscribe_key="sub-c-3bc37460-8899-11e5-bf00-02ee2ddab7fe")
  #
  # fetch the batches
  batches = get_batches(url)
  if batches == None or len(batches) == 0:
  	url = 'http://www.textfixer.com/resources/common-english-words.txt'
  	print "URL: " + url + "does not contain any words. Using this URL: " + url
  #
  #create psuedo devices for provisioning as though there were all publishing upon activation
  #
  id=0
  for b in batches:
    for w in b:
      id=id+1
      device_msg = create_json(id, w)
      print device_msg
      pubnub.publish(ch, device_msg)

