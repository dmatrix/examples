Mesos Command Sampler : The Hello World Example for Mesos Framework
=====================

This particular Mesos Framework is the equivalent of a Hello World—some example code snippet for a novice to get started with underlying interfaces for the Schdueler. 
It's derived and modeled (after RENDLER. Much of the code is borrowed and simplfied as an excercise for a 
novice to get his hands dirty and play around with the Mesos Framework Java API. More importantly, each platform, every language, and every framework
has its "Hello World" equivalent, this one is for Mesos just as WorCount is to MapReduce.


See also [RENDLER](http://github/mesosphere/RENDLER) for a more complete and complicated Framework as the second example.
Also, view the [accompanying slides](http://mesosphere.github.io/oscon-mesos-2014/#/) for more context.

Command Sampler consists of three main components:

- `SamplerExecutor` extends `mesos.Executor`
- `SamplerMain` the driver that connects to the Mesos Master`
- `SamplerScheduler` extends `mesos.Scheduler` and launches tasks with the executors

## Quick Start with Vagrant

### Requirements

- [VirtualBox](http://www.virtualbox.org/) 4.1.18+
- [Vagrant](http://www.vagrantup.com/) 1.3+
- [git](http://git-scm.com/downloads) (command line tool)

### Start the `mesos-demo` VM

```bash
$ wget http://downloads.mesosphere.io/demo/mesos.box -O /tmp/mesos.box
$ vagrant box add --name mesos-demo /tmp/mesos.box
$ git clone https://github.com/dmatrix/examples/mesos-sampler.git
$ cd mesos-sampler
$ vagrant up
```

Now that the VM is running, you can view the Mesos Web UI here:
[http://10.141.141.10:5050](http://10.141.141.10:5050)

You can see that 1 slave is registered and you've got some idle CPUs and Memory. So let's start the Command Sampler!

### Run Command Sampler Framework in the `mesos-demo` VM

Go to the java directory.

### Shutting down the `mesos-demo` VM

```bash
# Exit out of the VM
vagrant@mesos:hostfiles $ exit
# Stop the VM
$ vagrant halt
# To delete all traces of the vagrant machine
$ vagrant destroy
```
