
#!/usr/env python
from pubnub import pubnub
#

pubnub = Pubnub(publlish_key="demo", subscribe_key="demo")

#
# define some call backs
#
def callback(message, channel):
	print (message)

def error(message):
	print ("ERROR: " + str(message))

def connect(message):
	print ("CONNECTED")
	pubnub.publish(channel="my_channel", message="Hello World from PubNub's Python SDK")

def reconnect(message):
	print ("RECONNECTED")

def disconnect(message):
	print ("DISCONNECTED")

pubnub.subscribe(channels="my_channel", callback=callback, error=callback,
					connect=connect, reconnect=reconnect, disconnect=disconnect)