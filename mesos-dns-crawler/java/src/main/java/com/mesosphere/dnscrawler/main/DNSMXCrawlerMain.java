package com.mesosphere.dnscrawler.main;
import com.mesosphere.dnscrawler.scheduler.DNSMXCrawlerScheduler;
import com.mesosphere.dnscrawler.utils.DNSMXMesosUtils;
import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Scheduler;

import java.util.ArrayList;
import java.util.List;

import static com.mesosphere.dnscrawler.utils.DNSMXMesosUtils.getCommandInfo;

public class DNSMXCrawlerMain {

  private static void usage() {

    String name = DNSMXCrawlerMain.class.getName();
    System.err.println("Usage: " + name + " 127.0.1.1:5050 domain1.... domainN");
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      usage();
      System.exit(1);
    }
    String mesosMaster = args[0];
    List<String> domainTasks = new ArrayList<String>();
    for (int i = 1; i < args.length; i++) {
      domainTasks.add(args[i]);
    }
    // The the main jar and executor classes that will run on the node and
    // create a Task for
    // execution.
    String jarPath = "/home/vagrant/hostfiles/java/target/dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar";
    String executorMXArgs = "java -cp dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.dnscrawler.executors.DNSMXExecutor";
    String executorTlSArgs = "java -cp dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.dnscrawler.executors.DNSMXTLSCheckerExecutor";

    CommandInfo commandInfoMX = getCommandInfo(executorMXArgs, jarPath);
    CommandInfo commandInfoTLS = getCommandInfo(executorTlSArgs, jarPath);

    // Create the executor that will contact the DNS for MX records
    ExecutorInfo dnsMXExecutor = DNSMXMesosUtils.getExecutorInfo(commandInfoMX, "DNSMXExecutor");
    ExecutorInfo dnsMXTLSExecutor = DNSMXMesosUtils.getExecutorInfo(commandInfoTLS,
        "DNSMXTLSCheckerExecutor");

    // create the the DNSMX crawler scheduler, which will schedule the
    // executors
    Scheduler scheduler = new DNSMXCrawlerScheduler(dnsMXExecutor, dnsMXTLSExecutor, domainTasks);

    // this driver will talk to the Mesos master
    MesosSchedulerDriver driver = null;
    FrameworkInfo frameworkInfo = null;

    frameworkInfo = DNSMXMesosUtils.getFrameworkInfo("dnscrawler-framework-java");
    driver = new MesosSchedulerDriver(scheduler, frameworkInfo, mesosMaster);
    // while the framework is running on the master, it will receive offers
    // events and messages status from the Tasks
    // launched by the Executor.
    int status = driver.run() == Status.DRIVER_STOPPED ? 0 : 1;
    // Ensure that the driver process terminates.
    driver.stop();
    System.exit(status);
  }

}
