package com.mesosphere.dnscrawler.utils;

import org.apache.mesos.Protos;

/**
 * Created by jdamji on 9/17/15.
 */
public class DNSMXMesosUtils {
  public static Protos.CommandInfo.URI getURI(String path) {
    // Build your CommandInfo structure that maps to ProtocolBuffer
    // part of the message to the master via the protocol buffer
    // Ordinarily, you would have this jars packaged and installed on the
    // slave-nodes
    // on the Vagrant Virtual box, we are sharing folders so all the nodes have
    // access to these
    // binaries or jar files.
    // maps to a protocol buffer CommandInfo
    Protos.CommandInfo.URI.Builder uriBuilder = Protos.CommandInfo.URI.newBuilder();
    uriBuilder.setValue(path);
    uriBuilder.setExtract(false);
    return uriBuilder.build();
  }

  // Build your CommandInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  public static Protos.CommandInfo getCommandInfo(String command, String path) {
    Protos.CommandInfo.Builder cmdInfoBuilder = Protos.CommandInfo.newBuilder();
    cmdInfoBuilder.setValue(command);
    cmdInfoBuilder.addUris(getURI(path));
    return cmdInfoBuilder.build();
  }

  // Build your ExecutorInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  public static Protos.ExecutorInfo getExecutorInfo(Protos.CommandInfo cInfo, String executorName) {

    Protos.ExecutorInfo.Builder builder = Protos.ExecutorInfo.newBuilder();
    builder.setExecutorId(Protos.ExecutorID.newBuilder().setValue(executorName));
    builder.setCommand(cInfo);
    builder.setName("DNS MX " + executorName + "(Java)");
    builder.setSource("java");
    return builder.build();
  }

  // Build your FrameworkInfo structure that maps to ProtocolBuffer
  // part of the message to the master via the protocol buffer
  public static Protos.FrameworkInfo getFrameworkInfo(String pFrameName) {
    Protos.FrameworkInfo.Builder builder = Protos.FrameworkInfo.newBuilder();
    builder.setFailoverTimeout(120000).setUser("").setName(pFrameName);
    return builder.build();
  }
}
