
#!/usr/env python
from pubnub import Pubnub
import time
import sys
#
# initate the Pubnub instance
# subsistute your publish_key and subscribe_key here
#
pubnub = Pubnub(publish_key="demo", subscribe_key="demo")

#
# define some callbacks 
#
def callback(message, channel):
	print (message)

def error(message):
	print ("ERROR: " + str(message))

def connect(message):
	print ("CONNECTED")
	messages = ["Hello World from PubNub's Python SDK", 
				"Not bad for a quick examples", 
				"TTYL...",
				"{'time': '2:07:04pm', 'price': '510.83', 'delta': '69.02', 'perc': '13.51', 'vol': 2930}"]
	for msg in messages:
		pubnub.publish(channel="my_channel", message=msg)
		print "Napping..."
		time.sleep(1)

def reconnect(message):
	print ("RECONNECTED")

def disconnect(message):
	print ("DISCONNECTED")
#
# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
# channel
#
pubnub.subscribe(channels="my_channel", callback=callback, error=callback,
					connect=connect, reconnect=reconnect, disconnect=disconnect)
sys.exit(0)
