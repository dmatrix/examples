package com.mesosphere.dnscrawler.scheduler;

/*
 *  @author: Jules S. Damji
 *
 *  
 *  This code is reentrant.
 *  
 */
import java.util.ArrayList;
import java.util.Collection;
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

import com.google.protobuf.ByteString;

public class DNSMXCrawlerScheduler implements Scheduler {

  private int launchedTasks = 0;
  private int finishedTasks = 0;
  private int maxTasks = 1;
  private final ExecutorInfo executorDNS;
  private List<String> domainTasks;

  public DNSMXCrawlerScheduler(ExecutorInfo dnsMXExecutor, List<String> domainTasks) {
    this.executorDNS = dnsMXExecutor;
    this.domainTasks = domainTasks;
    this.maxTasks = domainTasks.size();
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

  @Override
  // Handle Offer events! Vital for Executors to launch task and the resource it
  // will need.
  //
  public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
    List<TaskInfo> tasks = new ArrayList<TaskInfo>();
    Collection<OfferID> taskOffers = new ArrayList<OfferID>();
    if (launchedTasks <= maxTasks) {
      for (Offer offer : offers) {
        String domain = domainTasks.get(launchedTasks++);
        taskOffers.add(offer.getId());
        TaskID taskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks)).build();
        System.out.println(String.format("Launching task for domain %s and id %s", domain,
            taskId.getValue()));
        // Task builder for Sampler and set the resource
        TaskInfo task = TaskInfo
            .newBuilder()
            .setName("task " + taskId.getValue())
            .setTaskId(taskId)
            .setSlaveId(offer.getSlaveId())
            .setData(ByteString.copyFromUtf8(domain))
            .addResources(
                Resource.newBuilder().setName("cpus").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(1)))
            .addResources(
                Resource.newBuilder().setName("mem").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(128)))
            .setExecutor(ExecutorInfo.newBuilder(executorDNS)).build();

        taskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks)).build();

        tasks.add(task);
      }
      driver.launchTasks(taskOffers, tasks);
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
      if (finishedTasks == maxTasks) {
        // done stop the driver
        System.out.println("All Executor Tasks Finished for all domains: " + finishedTasks);
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
  // and deal with as you like.
  public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId,
      byte[] data) {

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
