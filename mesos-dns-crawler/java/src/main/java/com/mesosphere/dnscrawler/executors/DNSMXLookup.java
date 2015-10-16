package com.mesosphere.dnscrawler.executors;

import java.util.ArrayList;
import java.util.List;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.*;
/**
 * @author jdamji
 * 
 */
public class DNSMXLookup {

  private String domain;
  /**
	 * 
	 */
  public DNSMXLookup(String domain) {
    this.domain = domain;
  }

  public String[] getMXHosts() {
    List<String> hosts = new ArrayList<String>();
    Record[] records;
    try {
      records = new Lookup(domain, Type.MX).run();
    } catch (TextParseException e) {
      e.printStackTrace();
      return null;
    }
    if (records != null && records.length != 0) {
      for (int i = 0; i < records.length; i++) {
        MXRecord mx = (MXRecord) records[i];
        hosts.add(mx.getTarget().toString());
      }
      return hosts.toArray(new String[hosts.size()]);
    } else {
      return null;
    }
  }
}
