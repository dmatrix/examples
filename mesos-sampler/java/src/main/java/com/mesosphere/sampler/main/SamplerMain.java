package com.mesosphere.sampler.main;

/*
 *  @author Jules S. Damji
 *  This framework is derived and modelled after RENDLER (http://github.com/mesosphere/RENDLER). Much of the code is borrowed 
 *  and simplified to write a sample Mesos framework as an excercise for a novice to learn the API.
 *  
 *  The gist of the framework is to execute create tasks and execute command. Note that this execution of commmand scan be done quite 
 *  easily and seamlessly with cronos or marathon. Nonetheless, for a getting-started, hands-on excercise, this a good place to start. 
 *  One can extend by providing command line argumesnt to command to the framework. By default it executes a innocous command 
 *  /bin/id. 
 */
import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.Credential;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Scheduler;

import com.google.protobuf.ByteString;
import com.mesosphere.sampler.scheduler.SamplerScheduler;

public class SamplerMain {

  private static void usage() {
    String name = SamplerScheduler.class.getName();
    System.err.println("Usage: " + name + " 127.0.1.1:5050 <tasks>");
  }
  public static void main(String[] args) throws Exception {
    if (args.length < 1 || args.length > 3) {
      usage();
      System.exit(1);
    }
    // get usr directory where this command is excuted and construct a path:
    // part of the message to the master
    // via the protocol buffer
    String path = System.getProperty("user.dir")
        + "/target/sampler-1.0-SNAPSHOT-jar-with-dependencies.jar";

    CommandInfo.URI uri = CommandInfo.URI.newBuilder().setValue(path).setExtract(false).build();

    // The executor that will run on the node and create a Task for execution.
    String commandSampler = "java -cp sampler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.sampler.executors.SamplerExecutor";
    CommandInfo commandInfoSampler = CommandInfo.newBuilder().setValue(commandSampler).addUris(uri)
        .build();

    // Create only one Executor for the command. One may create multiple
    // executors.
    ExecutorInfo executorSampler = ExecutorInfo.newBuilder()
        .setExecutorId(ExecutorID.newBuilder().setValue("SamplerExecutor"))
        .setCommand(commandInfoSampler).setName("Sampler Command Executor (Java)")
        .setSource("java").build();

    FrameworkInfo.Builder frameworkBuilder = FrameworkInfo.newBuilder().setFailoverTimeout(120000)
        .setUser("") // Have Mesos fill in
        // the current user.
        .setName("Sampler Command Framework (Java)");

    if (System.getenv("MESOS_CHECKPOINT") != null) {
      System.out.println("Enabling checkpoint for the framework");
      frameworkBuilder.setCheckpoint(true);
    }

    Scheduler scheduler = new SamplerScheduler(executorSampler, Integer.parseInt(args[1]));

    // this driver will talk to the Mesos master
    MesosSchedulerDriver driver = null;
    // TODO: need clearification for these authentication enviroment variables
    if (System.getenv("MESOS_AUTHENTICATE") != null) {
      System.out.println("Enabling authentication for the framework");

      if (System.getenv("DEFAULT_PRINCIPAL") == null) {
        System.err.println("Expecting authentication principal in the environment");
        System.exit(1);
      }

      if (System.getenv("DEFAULT_SECRET") == null) {
        System.err.println("Expecting authentication secret in the environment");
        System.exit(1);
      }
      Credential credential = Credential.newBuilder()
          .setPrincipal(System.getenv("DEFAULT_PRINCIPAL"))
          .setSecret(ByteString.copyFrom(System.getenv("DEFAULT_SECRET").getBytes())).build();

      frameworkBuilder.setPrincipal(System.getenv("DEFAULT_PRINCIPAL"));

      // Create a Mesos scheduler and provide our scheduler to it. The object
      // will interact with the Mesos Master
      driver = new MesosSchedulerDriver(scheduler, frameworkBuilder.build(), args[0], credential);
    } else {
      frameworkBuilder.setPrincipal("test-framework-java");
      driver = new MesosSchedulerDriver(scheduler, frameworkBuilder.build(), args[0]);
    }

    // while the framework is running on the master, it will receive offers
    // events and messages status from the Tasks
    // launched by the Executor.
    int status = driver.run() == Status.DRIVER_STOPPED ? 0 : 1;

    // Ensure that the driver process terminates.
    driver.stop();
    System.exit(status);
  }

}
