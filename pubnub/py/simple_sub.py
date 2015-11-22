
#!/usr/bin/env python
from pubnub import Pubnub
import time
import sys
import os
#
# initate the Pubnub instance
# subsistute your publish_key and subscribe_key here, if you want more security. But demo should work
#
pubnub = None

#
# define some callbacks
#
def sub_callback(message, channel):
	print (channel + ":" + message)

def on_error(message):
	print ("ERROR: " + str(message))

#
# callback when connected to the PubHub network
#
def on_reconnect(message):
	print ("RECONNECTED")
#
# callback whtn disconnected from the PubHub network
def on_disconnect(message):
	print ("DISCONNECTED")

if __name__ == "__main__":
#
# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
# channel
#
	pub_key=os.environ['PUB_KEY']
	sub_key=os.environ['SUB_KEY']
	print ("PUB_KEY=%s; SUB_KEY=%s" % (pub_key, sub_key))
	pubnub = Pubnub(publish_key=pub_key, subscribe_key=sub_key)
	pubnub.subscribe(channels="json_channel", callback=sub_callback, error=on_error, reconnect=on_reconnect, disconnect=on_disconnect)
	pubnub.start()
