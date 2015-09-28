#!/usr/bin/python

import sys, getopt
import traceback

class Trades ():
	def __init__(self, trade):
		self._open_close = trade[0]
		self.high_low = trade[1]
		self._volume = trade[2]
		self._adj_close = trade[3]
	
	def get_low_high(l_h):
		return self._low_high 

	def get_open_close(o_c):
		return self._open_close

	def get_volume(v):
		return self._volume

	def __str__(self):
		return str ((self._open_close, self.high_low, self._volume, self._adj_close))

	def __repr__(self):
		return self.__str__()

def simple_dump(d):
	for  kv in d.items():
		print kv[0], '->', kv[1]

def dump_clean(obj):
    if type(obj) == dict:
        for k, v in obj.items():
            if hasattr(v, '__iter__'):
                print k
                dump_clean(v)
            else:
                print '%s : %s' % (k, v)
    elif type(obj) == list:
        for v in obj:
            if hasattr(v, '__iter__'):
                dump_clean(v)
            else:
                print v
    else:
        print obj

def main (argv):
	inputfile = ''
	verbose = False
	try:
		opts, args = getopt.getopt(argv,"vi:",["ifile="])
	except getopt.GetoptError:
		print 'read_stock.py [-v] -i <inputfile>'
		sys.exit(2)
	for opt, arg in opts:
		if opt == '-h':
			print 'read_stock.py [-v] -i <inputfile>'
			sys.exit()
		elif opt == '-v':
			verbose = True
		elif opt in ("-i", "--ifile"):
			inputfile = arg
	print 'Reading the input file is "', inputfile

	try:
		trades = {}
		f = open(inputfile, 'r')
		cnt = 0
		for line in f:
			if line[0] == '#':
				continue
			line = line.strip()
			tokens = line.split(',')
			trade_blob = ( (tokens[1], tokens[4]), (tokens[2], tokens[3]), tokens[5], tokens[6] )
			trades[tokens[0]] = Trades(trade_blob)
			cnt=cnt+1
		print "\nTotal lines read ", cnt
		if verbose:
			simple_dump(trades)
		
	except Exception, err:
		print(traceback.format_exc())
	f.close()

if __name__ == "__main__":
   main(sys.argv[1:])
