package com.mesosphere.dnscrawler.scheduler;
/*
 *  @author: Jules S. Damji
 *
 *  
 *  This code is reentrant.
 *  
 */
import java.util.*;

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
  private ExecutorInfo executorDNS;
  private ExecutorInfo executorTLS;
  private List<String> domainTasks;
  private List<String> mxHostsQ;

  public DNSMXCrawlerScheduler(ExecutorInfo dnsMXExecutor, ExecutorInfo executorTLS,
      List<String> domainTasks) {
    this.executorDNS = dnsMXExecutor;
    this.executorTLS = executorTLS;
    this.domainTasks = domainTasks;
    this.mxHostsQ = Collections.synchronizedList(new ArrayList<String>());
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
    for (Offer offer : offers) {
      List<TaskInfo> tasks = new ArrayList<TaskInfo>();
      Collection<OfferID> taskOffers = new ArrayList<OfferID>();
      if (domainTasks.size() != 0) {
        String domain = domainTasks.remove(0);
        taskOffers.add(offer.getId());
        TaskID taskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks++)).build();
        System.out.println(String.format("Launching task for domain %s and id %s", domain,
            taskId.getValue()));
        // Task builder for DNSCrawler to fetch all MX records for each domain
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
        tasks.add(task);
        // create as Task for the TLS checker executor
      } else if (mxHostsQ.size() != 0) {
        String mxHost = mxHostsQ.remove(0);
        taskOffers.add(offer.getId());
        TaskID tlsTaskId = TaskID.newBuilder().setValue(Integer.toString(launchedTasks * 2))
            .build();
        TaskInfo mxTask = TaskInfo
            .newBuilder()
            .setTaskId(tlsTaskId)
            .setName("task " + mxHost + " " + tlsTaskId.getValue())
            .setSlaveId(offer.getSlaveId())
            .setData(ByteString.copyFromUtf8(mxHost))
            .addResources(
                Resource.newBuilder().setName("cpus").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(1)))
            .addResources(
                Resource.newBuilder().setName("mem").setType(Value.Type.SCALAR)
                    .setScalar(Value.Scalar.newBuilder().setValue(128)))
            .setExecutor(ExecutorInfo.newBuilder(executorTLS)).build();
        System.out.println(String.format("Launching TLS task %s for MX host %s",
            tlsTaskId.getValue(), mxHost));
        System.out.println(String.format("Current MX hosts in the Queue=%d", mxHostsQ.size()));
        tasks.add(mxTask);
      }
      if (tasks.size() > 0)
        driver.launchTasks(taskOffers, tasks);
    }
  }
  @Override
  public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
  }

  @Override
  // Check for completion of tasks. This call back is invoked when status Event
  // is received. Here you want do the relevant account keeping.
  //
  public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
    if (status.getState() == TaskState.TASK_FINISHED || status.getState() == TaskState.TASK_LOST) {
      System.out.println("Status update: task " + status.getTaskId().getValue()
          + " has completed with state " + status.getState());
      finishedTasks++;
      System.out.println("Finished tasks: " + finishedTasks);
      if (mxHostsQ.size() == 0) {
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
  // and deal with it. Here we want to insert into the Queue for the next executor task, TLS check
  public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId,
      byte[] data) {
        String mxHost = new String(data);
        // add to the TLS Executor tasks' queue
        mxHostsQ.add(mxHost);
  }

  @Override
  public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
      System.out.print(String.format("Slave host name %s lost", slaveId.getValue()));
  }

  @Override
  public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId,
      int status) {
    System.out.println("Executor lost: " + executorId.toString());
  }

  @Override
  public void error(SchedulerDriver driver, String message) {
    System.out.println("Error: " + message);
  }

}
