#!/usr/bin/python

import sys
import getopt
import traceback
import urllib2

def get_ip_info(ip):
    # get IP info codes from FreeGeoIP
    url = 'http://freegeoip.net/csv/' + ip
    str = urllib2.urlopen(url).read()
    return str

def main (argv):
    ipfile = None
    ip_f = None
    out_f = None
    err_f = None
    outfile ="../data/ips_info.txt"
    errfile = "../data/ips_unavailable_info.txt"
    #
    # parse command line args
    #
    try:
        opts, args = getopt.getopt(argv,"i:",["help", "ifile="])
    except getopt.GetoptError:
        print 'extract_ips_info.py -i <ips_file>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'extract_ips_info.py -i <ips_file>'
            sys.exit()
        elif opt in ("-i", "--ifile"):
            ipfile = arg
    #
    # get some fd open
    #
    try:
        ip_f  = open(ipfile, 'r')
        out_f = open(outfile, 'a+')
        err_f = open(errfile, 'a+')
    except Exception, err:
        print(traceback.format_exc())
        sys.exit(2)
    #
    # process each IP and fetch its info from FreeGeoIP
    #
    for ip in ip_f:
        try:
            l = get_ip_info(ip)
            print "Writing:" + l
            out_f.write(l)
        except urllib2.HTTPError, herr:
            #
            # append the unprocessed IP to the file
            # so we can reprocess it later
            #
            if (herr.code == 503):
                print "Service unavailable for " + ip
                err_f.write(ip)
                pass
            #
            # We passed our hourly quota
            #
            elif (herr.code == 403):
                print "Service exceeded quota!"
                sys.exit(2)
    #
    # close all file descriptors
    #
    if (ip_f):
        ip_f.close()
    if (out_f):
        out_f.close()
    if (err_f):
        err_f.close()

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print 'extract_ips_info.py -i <ips_file>'
        sys.exit()
    #
    # run the main driver
    #
    main(sys.argv[1:])
    sys.exit(0)

