
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
def sub_callback(message, channel):
	print (message)

def error(message):
	print ("ERROR: " + str(message))

def connect(message):
	print ("CONNECTED")
	messages = [
				"Hello World from PubNub's Python SDK", 
				"Not bad for a quick examples", 
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
		pubnub.publish(channel="my_channel", message=msg)
		time.sleep(1)

def reconnect(message):
	print ("RECONNECTED")

def disconnect(message):
	print ("DISCONNECTED")
#
# subscribe to a channel and invoke the appropriate callback when a message arrives on that 
# channel
#
pubnub.subscribe(channels="my_channel", callback=sub_callback, error=sub_callback,
					connect=connect, reconnect=reconnect, disconnect=disconnect)

