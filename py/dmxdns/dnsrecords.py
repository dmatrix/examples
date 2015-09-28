__author__ = 'jdamji'

import dns.resolver
#
# A wrapper class around DNS resolveer to fetch any kind of DNS record. MX, CNAME, SVR etc
# This class depends in Python package dnspython installed.
# You can get and install the package from http://www.dnspython.org/ and class documentation for the
# pacakge at http://www.dnspython.org/docs/1.11.0/
#

class DnsRecords:

    def __init__(self, name=None):
        self.name = name

    def get_record_types(self, name, type):
        if type == 'MX':
            records = self.get_mx_records(name)
        elif type == 'CNAME':
            records = self.get_cname_records(name)
        elif type == 'SVR':
            records = self.get_service_records(name)
            #
            #didnt match any supported record type
            # return an empty list
        else:
            records=[]
        return records

    def get_mx_records(self, name):
        mx=[]
        try:
            answers = dns.resolver.query(name, 'MX')
            for rdata in answers:
                mx.append(rdata.exchange.to_text())
        except dns.resolver.NXDOMAIN:
            pass
        finally:
            return mx

    def get_cname_records(self, name):
        return []

    def get_service_records(self, name):
        return []




