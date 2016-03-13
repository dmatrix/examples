#!/usr/bin/python
import sys, getopt
import traceback

#
# main driver of the programs reads a CSV text file with ips + other erroneous information.
# we want to extract all the ips.
def main (argv):
   inputfile = ''
   try:
      opts, args = getopt.getopt(argv,"i:",["ifile="])
   except getopt.GetoptError:
      print 'extract_ips.py -i <inputfile>'
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print 'extract_ips.py -i <inputfile>'
         sys.exit()
      elif opt in ("-i", "--ifile"):
         inputfile = arg

   try:
      f = open(inputfile, 'r')
      for line in f:
         if line[0] == '#':
            continue
         tokens = line.split()
         print(tokens[0])
   except Exception, err:
      print(traceback.format_exc())
   finally:
      f.close()

if __name__ == "__main__":
   if len(sys.argv) < 2:
      print 'extract_ips.py -i <inputfile>'
      sys.exit()

   main(sys.argv[1:])
