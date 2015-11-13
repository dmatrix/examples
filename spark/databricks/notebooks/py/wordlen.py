#!/usr/env python
# this small Spark application reads any texfile and computes the number of words per line and sorts them into an ascending order
# It filters out any all empty lines or lines with words == 0
# This is an excercise for me to learn how to write a simple spark app, submit it as an application, and run in the mode you wish.
# author: Jules S. Damji
#
from pyspark import SparkContext, SparkConf
import sys

if __name__ == "__main__":
	# given a line return the number of words in the line
	def getNumWords(line):
		if len(line) == 0:
			return 0
		words = line.split()
		#return number of words per line
		return len(words)
	#
	# create spark configuration and spark context, by speicify application name and mode
	#
	conf = SparkConf().setAppName("wordlen").setMaster("local")
	sc = SparkContext(conf=conf)
	#
	# read the file from the command line argument
	#
	if len(sys.argv) > 1:
		fname = sys.argv[1]
	else:
		print "usage: wordlen <filename>"
		sys.exit(1)
	#
	# create an rdd of lines
	#
	rddLines = sc.textFile(fname)
	#
	# create an rdd of number of words on each line
	# use our closure or lambda function that takes in a line and return the number of words
	rddWordLen = rddLines.map(getNumWords).filter(lambda n: n > 0)
	#
	# print out the first 10 words lens
	#
	print rddWordLen.take(25)
	# sort the list on the driver side and print 25 elements
	sortedWords = rddWordLen.collect()
	sortedWords.sort()
	#print it.
	print sortedWords

