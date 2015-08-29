package com.mesosphere.sampler.executors;

/**
 * @author Jules S. Damji
 * This Executor will create Task on the node on which it's created. It will register itself with the Mesos Master, 
 * will send an Event back to the Framework Scheduler
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

public class SamplerExecutor implements Executor {

  String fileName;
  String nodeName;

  @Override
  public void registered(ExecutorDriver driver, ExecutorInfo executorInfo,
      FrameworkInfo frameworkInfo, SlaveInfo slaveInfo) {
    nodeName = slaveInfo.getHostname();
    // Executor running on a node.
    System.out.println("Registered executor on " + nodeName);
  }

  @Override
  public void reregistered(ExecutorDriver driver, SlaveInfo executorInfo) {
  }

  @Override
  public void disconnected(ExecutorDriver driver) {
  }

  @Override
  // this the actual Task that will run. Here I'm simply executing a command.
  // But in reality it could anything you want, within compute and sanity
  // limitations.
  // Unless you're anti-social, /bin/rm -f -r or something malicious on the node
  // is not a good idea :)
  //
  public void launchTask(ExecutorDriver pDriver, TaskInfo pTaskInfo) {

    // Start task with status running
    TaskStatus status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_RUNNING).build();
    // send the Event Up the chain
    pDriver.sendStatusUpdate(status);
    // Run UNIX command
    fileName = new StringBuffer("/tmp/").append(nodeName).append("-")
        .append(pTaskInfo.getTaskId().getValue()).toString();
    StringBuffer cmdBuffer = new StringBuffer();
    // cmdBuffer.append("ls /proc | grep ^[0-9]");
    cmdBuffer.append("/usr/bin/id");
    String cmd = cmdBuffer.toString();

    try {
      int exitValue = runProcess(cmd);
      // If successful, send the framework message
      if (exitValue == 0) {
        String myStatus = "sampler:" + fileName;
        pDriver.sendFrameworkMessage(myStatus.getBytes());
      }
    } catch (Exception e) {
      System.out.println("Exception executing : " + e);
    }

    // Set the task with status finished
    status = TaskStatus.newBuilder().setTaskId(pTaskInfo.getTaskId())
        .setState(TaskState.TASK_FINISHED).build();
    pDriver.sendStatusUpdate(status);

  }

  /**
   * Print lines for any input stream, i.e. stdout or stderr.
   * 
   * @param name
   *          the label for the input stream
   * @param ins
   *          the input stream containing the data
   */
  private void printLines(String name, InputStream ins) throws Exception {
    String line = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(ins));
    while ((line = in.readLine()) != null) {
      System.out.println(name + " " + line);
    }
  }

  /**
   * Execute a command with error logging.
   * 
   * @param the
   *          string containing the command that needs to be executed
   */
  private int runProcess(String command) throws Exception {
    Process pro = Runtime.getRuntime().exec(command);
    printLines(command + " stdout:", pro.getInputStream());
    printLines(command + " stderr:", pro.getErrorStream());
    pro.waitFor();
    System.out.println(command + " exitValue() " + pro.exitValue());
    return pro.exitValue();
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
  }

  public static void main(String[] args) throws Exception {
    MesosExecutorDriver driver = new MesosExecutorDriver(new SamplerExecutor());

    System.exit(driver.run() == Status.DRIVER_STOPPED ? 0 : 1);
  }

}
