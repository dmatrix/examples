#!/usr/bin/env python
from pubnub import Pubnub
import sys
import json
"""
This short example illustrates the simplicity of using PubNub as a Publish-Subscribe cloud service. 
As an example, this program simulates as though multiple devices are registering themselves or announcing their
availability by publishing a channel. It subscribes to that publishing channels and receives its JSON object

Also, as an extension it can write to a socket where a Spark Streaming context is connected to process
live data streams of JSON objects from each device. Alternatively, it can write to a file in the directory
where a Spark streaming context is monitoring any data files.

Each JSON object received on its subscribed channles is the following format.

It downloads a list of words from the Internet (http://www.textfixer.com/resources/common-english-words.txt) and uses them as device names. Each JSON object has the 
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
author: Jules S. Damji 
"""
#
#
# define some callbacks
#
def receive(message, channel):
	json.dumps(message)

def on_error(message):
	print ("ERROR: " + str(message))

if __name__ == "__main__":
	if len(sys.argv) != 2:
		print("Usage: sub_dev_words.py channel")
		sys.exit(-1)
	ch = sys.argv[1]
  	#
  	#initialize the PubNub handle
  	#
  	pubnub = Pubnub(publish_key="YOUR PUB KEY", subscribe_key="YOUR SUB KEY")

	# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
	# channel
	#
	print("Subscribing from PubNub Channel '%s'" % (ch))
	pubnub.subscribe(channels=ch, callback=receive, error=on_error)
	pubnub.start()
