
#!/usr/env python
from pubnub import Pubnub
import time
import sys
#
# initate the Pubnub instance
# subsistute your publish_key and subscribe_key here, if you want more security. But demo should work
#
pubnub = Pubnub(publish_key="demo", subscribe_key="demo")

#
# define some callbacks
#
def sub_callback(message, channel):
	print (channel + ":" + message)

def on_error(message):
	print ("ERROR: " + str(message))

def on_connect_json(message):
	print ("JSON CONNECTED")
	messages = [
				"Hello World from PubNub's Python SDK",
				"Some Geo location data:",
				"{ 'vehicle_id: 10, 'lat': -37.123, 'lng':120.456}",
				"{ 'vehicle_id: 11, 'lat': -38.123, 'lng':121.456}",
				"{ 'vehicle_id: 12, 'lat': -39.123, 'lng':122.456}",
				"{ 'vehicle_id: 13, 'lat': -40.123, 'lng':123.456}",
				"Here are some stock quotes:",
				"{'stock': 'APPL', time': '2:07:04pm', 'price': '510.83', 'delta': '69.02', 'perc': '13.51', 'vol': 2930}",
				"{'stock': 'IBM', time': '2:07:04pm', 'price': '610.83', 'delta': '79.02', 'perc': '15.51', 'vol': 293000}",
				"{'stock': 'CISCO', time': '2:07:04pm', 'price': '530.83', 'delta': '89.02', 'perc': '83.51', 'vol': 42930}",
				"{'stock': 'HP', time': '2:07:04pm', 'price': '510.83', 'delta': '69.02', 'perc': '13.51', 'vol': 32930}",
				"{'stock': 'GOOGL', time': '2:07:04pm', 'price': '610.83', 'delta': '69.02', 'perc': '13.51', 'vol': 930}",
				"TTYL..." 
				]
	for msg in messages:
		pubnub.publish(channel="json_channel", message=msg)

def on_connect_txt(message):
	print ("TEXT CONNECTED")
	messages = [ "Hello World from PubNub's Python SDK"]
	#
	# iterate over the list of messages and publish each one on each channel
	# have to figure out how to publish to multiple channels, Perhaps use a group
	#
	for msg in messages:
		pubnub.publish(channel="msg_channel", message=msg)
			
#
# callback when connected to the PubHub network
#
def on_reconnect(message):
	print ("RECONNECTED")
#
# callback whtn disconnected from the PubHub network
def on_disconnect(message):
	print ("DISCONNECTED")
#
# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
# channel
#
pubnub.subscribe(channels="json_channel", callback=sub_callback, error=on_error,
					connect=on_connect_json, reconnect=on_reconnect, disconnect=on_disconnect)
pubnub.start()
