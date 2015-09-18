package com.mesosphere.dnscrawler.executors;

/**
 * @author Jules S. Damji
 */
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.mesos.Executor;
import org.apache.mesos.ExecutorDriver;
import org.apache.mesos.MesosExecutorDriver;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.SlaveInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.TaskState;
import org.apache.mesos.Protos.TaskStatus;

public class DNSMXExecutor implements Executor {

  private String nodeName;
  private Map<String, Boolean> domainTasks;

  public DNSMXExecutor(String[] args) {
    domainTasks = new HashMap<String, Boolean>();
    for (String d : args) {
      domainTasks.put(d, false);
    }
  }

  @Override
  public void registered(ExecutorDriver driver, ExecutorInfo executorInfo,
      FrameworkInfo frameworkInfo, SlaveInfo slaveInfo) {
    nodeName = slaveInfo.getHostname();
    String executorName = getClass().getName();
    // Executor running on a node.
    System.out.println(String.format("Registered executor %s on node %s", executorName, nodeName));
  }

  @Override
  public void reregistered(ExecutorDriver driver, SlaveInfo executorInfo) {
  }

  @Override
  public void disconnected(ExecutorDriver driver) {
  }

  @Override
  public void launchTask(ExecutorDriver pDriver, TaskInfo pTaskInfo) {
    // Start task with status running
    TaskStatus status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_RUNNING).build();
    // send the Event Up the chain
    pDriver.sendStatusUpdate(status);
    // get the MX records
    String domain = pTaskInfo.getData().toStringUtf8();
    DNSMXLookup mx = new DNSMXLookup(domain);
    String[] mxHosts = mx.getMXHosts();
    if (mxHosts != null && mxHosts.length > 0) {
      for (String mxHost : mxHosts) {
        System.out.println(String.format("Domain %s: MX hosts %s", domain, mxHost));
        // send message to the Framework to Launch Task for the mxTLSChecker
        // and send each MX host to the scheduler, so that the scheduler can now
        // create tasks for TLS checks
        StringBuffer b = new StringBuffer(domain);
        b.append("|");
        b.append(mxHost);
        pDriver.sendFrameworkMessage(mxHost.getBytes());
      }
    } else {
      System.out.println(String.format("Domain %s: MX hosts failed to retrieve or does not exists",
          domain));
      // Set the task with status finished since we don't have any MX hosts to
      // check for TLS
    }
    status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_FINISHED).build();
    pDriver.sendStatusUpdate(status);
  }

  @Override
  public void killTask(ExecutorDriver driver, TaskID taskId) {
  }

  @Override
  public void frameworkMessage(ExecutorDriver driver, byte[] data) {
  }

  @Override
  public void shutdown(ExecutorDriver driver) {
  }

  @Override
  public void error(ExecutorDriver driver, String message) {
    System.out.println("Error: " + getClass().getName() + ": " + message);
  }

  public static void main(String[] args) throws Exception {
    MesosExecutorDriver driver = new MesosExecutorDriver(new DNSMXExecutor(args));

    System.exit(driver.run() == Status.DRIVER_STOPPED ? 0 : 1);
  }

}
