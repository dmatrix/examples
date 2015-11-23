
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

def on_error(message):
	print ("ERROR: " + str(message))

def publish_json_data():
	messages = [
				"Hello World from PubNub's Python SDK",
				"Some Geo location data:",
				"{'vehicle_id: 10, 'lat': -37.123, 'lng':120.456}",
				"{'vehicle_id: 11, 'lat': -38.123, 'lng':121.456}",
				"{'vehicle_id: 12, 'lat': -39.123, 'lng':122.456}",
				"{'vehicle_id: 13, 'lat': -40.123, 'lng':123.456}",
				"Here are some stock quotes:",
				"{'stock': 'APPL', time': '2:07:04pm', 'price': '510.83', 'delta': '69.02', 'perc': '13.51', 'vol': 2930}",
				"{'stock': 'IBM', time': '2:07:04pm', 'price': '610.83', 'delta': '79.02', 'perc': '15.51', 'vol': 293000}",
				"{'stock': 'CISCO', time': '2:07:04pm', 'price': '530.83', 'delta': '89.02', 'perc': '83.51', 'vol': 42930}",
				"{'stock': 'HP', time': '2:07:04pm', 'price': '510.83', 'delta': '69.02', 'perc': '13.51', 'vol': 32930}",
				"{'stock': 'GOOGL', time': '2:07:04pm', 'price': '610.83', 'delta': '69.02', 'perc': '13.51', 'vol': 930}",
				"TTYL..." 
				]
	for msg in messages:
		print ("publishg to %s : %s" % ("json_channel", msg))
		pubnub.publish(channel="json_channel", message=msg, error=on_error)

if __name__ == "__main__":
	pub_key=os.environ['PUB_KEY']
	sub_key=os.environ['SUB_KEY']
	pubnub = Pubnub(publish_key=pub_key, subscribe_key=sub_key)
	publish_json_data()
