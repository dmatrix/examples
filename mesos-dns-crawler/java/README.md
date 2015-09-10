# Java DNSMX crawler Framework

1. `mvn clean compile assembly:single`
1. `vagrant ssh`
1. cd hostfiles
1. `export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib`
1. `java -cp target/dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.dnscrawler.main.DNSMXCrawlerMain 127.0.1.1:5050 domain1 domain2 ... domainN `
