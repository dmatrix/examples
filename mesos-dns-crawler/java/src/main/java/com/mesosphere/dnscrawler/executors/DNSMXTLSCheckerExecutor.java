package com.mesosphere.dnscrawler.executors;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.mesos.Executor;
import org.apache.mesos.ExecutorDriver;
import org.apache.mesos.MesosExecutorDriver;
import org.apache.mesos.Protos.*;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created by jdamji on 9/15/15.
 */
public class DNSMXTLSCheckerExecutor implements Executor {

  @Override
  public void launchTask(ExecutorDriver pDriver, TaskInfo pTaskInfo) {

    // Start task with status running
    TaskStatus status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_RUNNING).build();
    // send the Event Up the chain
    pDriver.sendStatusUpdate(status);
    String mxHost = pTaskInfo.getData().toStringUtf8();
    System.out.println(String.format("Looking MX hosts %s", mxHost));
    Properties props = new Properties();
    props.setProperty("mail.smtp.host", mxHost);
    Session session = null;
    SMTPTransport transport = null;
    try {
      session = Session.getDefaultInstance(props);
      transport = (SMTPTransport) session.getTransport("smtp");
      boolean useTLS = transport.getStartTLS();
      if (useTLS) {
        System.out.println(String.format("MX host %s uses TLS", mxHost));
      } else {
        System.out.println(String.format("MX host %s does not use TLS", mxHost));
      }
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    } finally {
      if (transport != null) {
        try {
          transport.close();
        } catch (MessagingException e) {
          // ignore
        }
      }
    }
    status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_FINISHED).build();
    // send the Event Up the chain
    pDriver.sendStatusUpdate(status);
  }

  @Override
  public void disconnected(ExecutorDriver executorDriver) {

  }

  @Override
  public void error(ExecutorDriver executorDriver, String message) {
    System.out.println("Error: " + getClass().getName() + ": " + message);

  }

  @Override
  public void killTask(ExecutorDriver executorDriver, TaskID taskID) {

  }

  @Override
  public void registered(ExecutorDriver executorDriver, ExecutorInfo executorInfo,
      FrameworkInfo frameworkInfo, SlaveInfo slaveInfo) {
    String nodeName = slaveInfo.getHostname();
    String executorName = getClass().getName();
    // Executor running on a node.
    System.out.println(String.format("Registered executor %s on node %s", executorName, nodeName));

  }

  @Override
  public void reregistered(ExecutorDriver executorDriver, SlaveInfo slaveInfo) {

  }

  @Override
  public void frameworkMessage(ExecutorDriver executorDriver, byte[] bytes) {

  }

  @Override
  public void shutdown(ExecutorDriver executorDriver) {

  }

  public static void main(String[] args) throws Exception {
    MesosExecutorDriver driver = new MesosExecutorDriver(new DNSMXTLSCheckerExecutor());

    System.exit(driver.run() == Status.DRIVER_STOPPED ? 0 : 1);
  }
}
