package com.mesosphere.dnscrawler.test;

import com.mesosphere.dnscrawler.executors.DNSMXLookup;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by jdamji on 9/14/15.
 */
public class TestDNSJava {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Need at least one domain name");
      System.exit(1);
    }
    for (String d : args) {
      DNSMXLookup mx = new DNSMXLookup(d);
      String[] mxHosts = mx.getMXHosts();
      if (mxHosts != null && mxHosts.length > 0) {
        for (String h : mxHosts) {
            System.out.println(String.format("Looking up domain %s: MX hosts %s", d, h));
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", h);
            Session session = null;
            SMTPTransport transport = null;
            try {
                transport = (SMTPTransport) session.getTransport("smtp");
                session = Session.getDefaultInstance(props);
                boolean useTLS = transport.getStartTLS();
                if (useTLS) {
                    System.out.println(String.format("For domain %s MX host %s uses TLS", d, h));
                } else {
                    System.out.println(String.format("For domain %s MX host %s does not use TLS", d, h));
                }
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } finally {
                if (transport != null) {
                    try {
                        transport.close();
                    } catch (MessagingException e) {
                        //ignore
                    }
                }
            }
        }
      } else {
            System.out.println(String.format(
            "domain %s: MX hosts failed to retrieve or do not exist", d));
      }
    }
  }
}
