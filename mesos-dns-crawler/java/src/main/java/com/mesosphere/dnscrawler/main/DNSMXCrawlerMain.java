package com.mesosphere.dnscrawler.main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Scheduler;

import com.mesosphere.dnscrawler.scheduler.DNSMXCrawlerScheduler;

public class DNSMXCrawlerMain {

  private static void usage() {

    String name = DNSMXCrawlerMain.class.getName();
    System.err.println("Usage: " + name + " 127.0.1.1:5050 domain1.... domainn");
  }

  private static CommandInfo.URI getURI() {
    // Build your CommandInfo structure that maps to ProtocolBuffer
    // part of the message to the master via the protocol buffer
    // Ordinarily, you would have this jars packaged and installed on the
    // slave-nodes
    // on the Vagrant Virtual box, we are sharing folders so all the nodes have
    // access to these
    // binaries or jar files.
    String path = "/home/vagrant/hostfiles/java/target/dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar";
    // maps to a protocol buffer CommandInfo
    CommandInfo.URI.Builder uriBuilder = CommandInfo.URI.newBuilder();
    uriBuilder.setValue(path);
    uriBuilder.setExtract(false);
    return uriBuilder.build();
  }

  // Build your CommandInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  private static CommandInfo getCommandInfo(String command) {
    CommandInfo.Builder cmdInfoBuilder = Protos.CommandInfo.newBuilder();
    cmdInfoBuilder.setValue(command);
    cmdInfoBuilder.addUris(getURI());
    return cmdInfoBuilder.build();
  }

  // Build your ExecutorInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  private static ExecutorInfo getExecutorInfo(CommandInfo cInfo) {

    ExecutorInfo.Builder builder = ExecutorInfo.newBuilder();
    builder.setExecutorId(Protos.ExecutorID.newBuilder().setValue("DNSMXExecutor"));
    builder.setCommand(cInfo);
    builder.setName("DNS MX Executor (Java)");
    builder.setSource("java");
    return builder.build();
  }

  // Build your FrameworkInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  private static FrameworkInfo getFrameworkInfo(String principal) {
    FrameworkInfo.Builder builder = FrameworkInfo.newBuilder();
    builder.setFailoverTimeout(120000).setUser("").setName("DNS MX Crawler Framework (Java)");
    return builder.build();
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
    // The executor class that will run on the node and create a Task for
    // execution.
    StringBuffer executorArgs = new StringBuffer(
        "java -cp dnscrawler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.dnscrawler.executors.DNSMXExecutor");

    String commandMXExecutor = executorArgs.toString();
    CommandInfo commandInfoMX = getCommandInfo(commandMXExecutor);
    // Create the executor that will contact the DNS for MX records

    ExecutorInfo dnsMXExecutorDNSMX = getExecutorInfo(commandInfoMX);

    // create the the DNSMX crawler scheduler, which will scheduler the
    // executors
    Scheduler scheduler = new DNSMXCrawlerScheduler(dnsMXExecutorDNSMX, domainTasks);

    // this driver will talk to the Mesos master
    MesosSchedulerDriver driver = null;
    FrameworkInfo frameworkInfo = null;

    frameworkInfo = getFrameworkInfo("dnscrawler-framework-java");
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
