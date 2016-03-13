#!/usr/bin/python

import sys
import getopt
import traceback
import urllib2

cc2cc3_map = {}

def build_cc2cc3_map(f):
    for line in f:
        tokens = line.split(' ')
        cc2cc3_map[tokens[0]] = tokens[1]

def get_ip_info(f):
    for ip in f:
        url = 'http://freegeoip.net/csv/' + ip
        line = urllib2.urlopen(url).read()
        print line


def print_map(m):
    keys = m.keys()
    keys.sort()
    for k in keys:
        print k, m[k]

def main (argv):
    ipfile = ''
    ccfile = ''
    try:
        opts, args = getopt.getopt(argv,"hi:c:",["help", "ifile=", "cfile="])
    except getopt.GetoptError:
        print 'ips2ccodes.py -i <ips_file> -c <cc2_cc3_codes_file>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'ips2ccodes.py -i <ips_file> -c <cc2_cc3_codes_file>'
            sys.exit()
        elif opt in ("-i", "--ifile"):
            ipfile = arg
        elif opt in ("-c","--cfile"):
            ccfile = arg
    try:
        ip_f  = open(ipfile, 'r')
        cc_f = open(ccfile, 'r')
        build_cc2cc3_map(cc_f)

    except Exception, err:
        print(traceback.format_exc())
    finally:
        ip_f.close()
        cc_f.close()

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print 'ips2ccodes.py -i <ips_file> -c <cc2_cc3_codes_file>'
        sys.exit()

    main(sys.argv[1:])

