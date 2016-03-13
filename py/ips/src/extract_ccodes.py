#!/usr/bin/python
import sys, getopt
import traceback

#
# main driver that reads th CSV txt file with ISO-3991 cc-1 and cc-2 country codes.
# we wnat to extract those two items.
#
def main (argv):
   inputfile = ''
   try:
      opts, args = getopt.getopt(argv,"i:",["ifile="])
   except getopt.GetoptError:
      print 'extract_codes.py -i <inputfile>'
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print 'extract_ccodes.py [-v] -i <inputfile>'
         sys.exit()
      elif opt in ("-i", "--ifile"):
         inputfile = arg

   try:
      f = open(inputfile, 'r')
      for line in f:
         if line[0] == '#':
            continue
         tokens = line.split()
         print (tokens[0] + " " + tokens[1])
   except Exception, err:
      print(traceback.format_exc())
   finally:
      f.close()

if __name__ == "__main__":
   if len(sys.argv) < 2:
      print 'extract_ccodes.py [-v] -i <inputfile>'
      sys.exit()

   main(sys.argv[1:])