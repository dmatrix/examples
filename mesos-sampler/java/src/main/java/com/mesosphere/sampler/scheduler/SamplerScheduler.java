package com.mesosphere.sampler.scheduler;

/*
 *  @author: Jules S. Damji
 *  This scheduler implements Mesos Scheduler Interface. These are callback functions that will be invoked
 *  for events propogating up from slaves via Mesos master up to the Framework Scheduler. 
 *  
 *  (Much of the skeletal code is borrowed from http://github.com/mesosphere/RENDLER
 *  
 *  This code is reentrant.
 *  
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.TaskState;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Protos.Value;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;

public class SamplerScheduler implements Scheduler {

  private final ExecutorInfo executorSampler;
  private final int totalTasks;
  private int launchedTasks = 0;
  private int finishedTasks = 0;
  private String cmd;

  public SamplerScheduler(ExecutorInfo executorSampler, int totalTasks) {
    this.executorSampler = executorSampler;
    this.totalTasks = totalTasks;
  }
  public SamplerScheduler(ExecutorInfo executorSampler, String cmd, int totalTasks) {
    this.executorSampler = executorSampler;
    this.totalTasks = totalTasks;
    this.cmd = cmd;
  }

  @Override
  // This call back will be invoked by our driver when it receives a REGISTERED
  // EVENT
  // all message passing between our driver and Mesos master is via HTTP on
  // Protocol buffers.
  public void registered(SchedulerDriver driver, FrameworkID frameworkId, MasterInfo masterInfo) {
    // normally you will want to write to a log file for debugging purposes, but
    // simplicity we are putting out
    // to stdout.
    System.out.println("Registered! ID = " + frameworkId.getValue());
  }

  @Override
  // Under some circumstances due to timeout or lost of connectivity, the
  // framework will want to re-register
  // Implement this for retries or failures. For example, the Messos's master
  // might have failed over to a newer
  // master.
  public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {
  }

  @Override
  // should the driver be disconnected from the master, this event callback may
  // want to take some action.
  //
  public void disconnected(SchedulerDriver driver) {
  }

  @SuppressWarnings("deprecation")
  @Override
  // Handle Offer events! Vital for Executors to launch task and the resource it
  // will need.
  //
  public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
    for (Offer offer : offers) {
      List<TaskInfo> tasks = new ArrayList<TaskInfo>();
      // accept offers only if tasks have not completed
      //
      if (launchedTasks < totalTasks) {
        TaskID taskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks++)).build();

        System.out.println("Launching task " + taskId.getValue());
        // Task builder for Sampler and set the resource
        TaskInfo task = TaskInfo
            .newBuilder()
            .setName("task " + taskId.getValue())
            .setTaskId(taskId)
            .setSlaveId(offer.getSlaveId())
            .addResources(
                Resource.newBuilder().setName("cpus").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(1)))
            .addResources(
                Resource.newBuilder().setName("mem").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(128)))
            .setExecutor(ExecutorInfo.newBuilder(executorSampler)).build();

        taskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks++)).build();

        tasks.add(task);
      }
      driver.launchTasks(offer.getId(), tasks);
    }
  }

  @Override
  public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
  }

  @Override
  // Check for completion of tasks. This call back is invoked when status Event
  // is received. Here
  // you want update approviate account keeping.
  //
  public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
    if (status.getState() == TaskState.TASK_FINISHED || status.getState() == TaskState.TASK_LOST) {
      System.out.println("Status update: task " + status.getTaskId().getValue()
          + " has completed with state " + status.getState());
      finishedTasks++;
      System.out.println("Finished tasks: " + finishedTasks);
      if (finishedTasks == totalTasks) {
        // done stop the driver
        System.out.println("All Executor Tasks Finished: " + finishedTasks);
        System.out.println("Stopping the Framework Driver: We are done!");
        driver.stop();
      }
    } else {
      System.out.println("Status update: task " + status.getTaskId().getValue() + " is in state "
          + status.getState());
    }
  }

  @Override
  // The Executors can send Messages back to Scheduler. Handle in this callback
  // and deal with as
  // you like.
  public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId,
      byte[] data) {
    String messageStr = new String(data);
    if (messageStr.startsWith("sampler")) {
      System.out.println("Command Sampler Task executed on node: " + slaveId.getValue());
    }
  }

  @Override
  public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
  }

  @Override
  public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId,
      int status) {
  }

  @Override
  public void error(SchedulerDriver driver, String message) {
    System.out.println("Error: " + message);
  }

}
