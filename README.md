[![DOI](https://zenodo.org/badge/267887969.svg)](https://zenodo.org/badge/latestdoi/267887969)
# IoTCheck
This is the repository for IoTCheck, a framework that model-checks smart home apps. Please read our paper before using IoTCheck.

**Understanding and Automatically Detecting Conflicting Interactions between Smart Home Applications**\
*Rahmadi Trimananda, Seyed Amir Hossein Aqajari, Jason Chuang, Brian Demsky, Guoqing Harry Xu, and Shan Lu,*\
*Proceedings of the ACM SIGSOFT International Symposium on Foundations of Software Engineering 2020 (FSE 2020).*

The following instructions explain how to quickly get started with IoTCheck. For more information about IoTCheck components, please see the [Wiki page of this repository](https://github.com/uci-plrg/iotcheck/wiki).

## Getting Started
One easy way to install IoTCheck is using our [Vagrant-packaged IoTCheck](https://github.com/uci-plrg/iotcheck-vagrant). Nevertheless, if the necessary components are present on our system, we could also download IoTCheck directly from this repository and set it up correctly to run on our system (our team has developed IoTCheck on Ubuntu 16.04.6 LTS (Xenial Xerus). 
Please see [this script](https://github.com/uci-plrg/iotcheck-vagrant/blob/master/bootstrap.sh) and [this script](https://github.com/uci-plrg/iotcheck-vagrant/blob/master/data/setup.sh) for the necessary components and steps to correctly install IoTCheck after downloading it from this repository.

## Examples
We have prepared 2 example runs to quickly test that the IoTCheck installation is working perfectly and to understand the output of IoTCheck runs.

1. First, we need to get into the directory `iotcheck/smartthings-infrastructure`.

```
    $ cd iotcheck/smartthings-infrastructure/
```

2. Run the `iotcheck.sh` script for both non-conflict and conflict examples. Each example takes a few minutes to run and messages will be printed out on the screen.

```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -e exampleNonConflicts
```
```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -e exampleConflicts
```

3. The results of all IoTCheck runs are collected in `iotcheck/logs/`.

#### Non-Conflict Examples
The log files for the non-conflict example runs can be found in `iotcheck/logs/exampleNonConflicts`. There are two log files for two pairs: 

1. `good-night-house.groovy--nfc-tag-toggle.groovy.log` for the pair `good-night-house.groovy` and `nfc-tag-toggle.groovy`, and 
2. `good-night-house.groovy--single-button-controller.groovy.log` for the pair `good-night-house.groovy` and `single-button-controller.groovy`.

If we open one of the log files, e.g., `good-night-house.groovy--nfc-tag-toggle.groovy.log`, we can see the reported result printed as the following.

```
JavaPathfinder core system v8.0 (rev 61f7af9209cd126105042a7b12ace7e7eb3f5492) - (C) 2005-2014 
United States Government. All rights reserved.


====================================================== system under test
main.main()

====================================================== search started: 5/26/20 3:54 PM

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:00:10
states:             new=13,visited=14,backtracked=27,end=0
search:             maxDepth=5,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=13
heap:               new=589608,released=444025,maxLive=77394,gcCycles=27
instructions:       47693786
max memory:         1213MB
loaded code:        classes=786,methods=15362

====================================================== search finished: 5/26/20 3:54 PM
```

Both log files will show the line `no errors detected`, because there were no conflicts found between the two apps in each pair.

#### Conflict Examples
The log files for the conflict example runs can be found in `iotcheck/logs/exampleConflicts`. There are two log files for two pairs: 

1. `enhanced-auto-lock-door.groovy--nfc-tag-toggle.groovy.log` for the pair `enhanced-auto-lock-door.groovy` and `nfc-tag-toggle.groovy`, and 
2. `enhanced-auto-lock-door.groovy--single-button-controller.groovy.log` for the pair `enhanced-auto-lock-door.groovy` and `single-button-controller.groovy`.

If we open one of the log files, e.g., `enhanced-auto-lock-door.groovy--nfc-tag-toggle.groovy.log`, we can see the reported result printed as the following.

```
JavaPathfinder core system v8.0 (rev 61f7af9209cd126105042a7b12ace7e7eb3f5492) - (C) 2005-2014 
United States Government. All rights reserved.


====================================================== system under test
main.main()

====================================================== search started: 5/26/20 3:56 PM

====================================================== error 1
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.RuntimeException: Conflict found between the two apps. App1 has written the value: "locked" to 
the attribute: currentLock while App2 is writing the value: "unlocked" to the same variable!
        
        ...

====================================================== results
error #1: gov.nasa.jpf.vm.NoUncaughtExceptionsProperty "java.lang.RuntimeException: Conflict found between..."

====================================================== statistics
elapsed time:       00:00:07
states:             new=5,visited=2,backtracked=6,end=0
search:             maxDepth=3,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=2
heap:               new=497975,released=365064,maxLive=79104,gcCycles=6
instructions:       35487521
max memory:         1199MB
loaded code:        classes=796,methods=14914

====================================================== search finished: 5/26/20 3:57 PM
```

This log file shows the line

```
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.RuntimeException: Conflict found between the two apps. App1 has written the value: "locked" to 
the attribute: currentLock while App2 is writing the value: "unlocked" to the same variable!
```

which means that a conflict has been found between the two apps: `enhanced-auto-lock-door.groovy` is `App1` and `nfc-tag-toggle.groovy` is `App2`. `App1` wrote the value `locked` to the shared lock state variable `currentLock`, and `App2` is found to subsequently write to the same variable with the value `unlocked`. Since one attempts to lock and the other unlock the door, IoTCheck reports that these two apps have a conflict.

## Experiments
Our paper explains the 3 categories of interactions between smart home apps, namely **device interaction**, **physical-medium interaction**, and **global-variable interaction**. The details of our manual study and experimental results are documented [here](https://github.com/uci-plrg/iotcheck-data).
IoTCheck only prints out warning messages for potential conflicts in the **physical-medium interaction** category since detecting a conflict in this interaction category requires specific conditions (please read the paper for more information).
We use the script `iotcheck.sh` to run experiments in each category and group of apps. Typing `./iotcheck.sh` in the directory `iotcheck/smartthings-infrastructure` will print out the following help information on how to execute the individual experiments.

```
Usage:  iotcheck.sh [options]

        -h      (print this usage info)

        -e      exampleConflicts
                exampleNonConflicts

        -d      acfanheaterSwitches
                alarms
                cameras
                cameraSwitches
                dimmers
                hueLights
                lightSwitches
                locks
                musicPlayers
                nonHueLights
                relaySwitches
                speeches
                switches
                thermostats
                valves
                ventfanSwitches

        -g      globalStateVariables
```
All the log files produced by these runs are collected in the directory `iotcheck/logs/`. Each sub-directory contains the log files for the respective category. 

#### Forming Pairs
The script runs IoTCheck for pairs listed in the files stored in `iotcheck/smartthings-infrastructure/appLists/`.
For example, it uses the files `locksAppList` and `locksAppList2` in `iotcheck/smartthings-infrastructure/appLists/device-interaction` to form pairs in the `locks` group and run IoTCheck to model-check each pair. 
We can put the sign `#` in front of a specific app name in the file to tell IoTCheck to exclude that app in the pairing process. We can see this being done in the app list files in `iotcheck/smartthings-infrastructure/appLists/examples`. 
In the file `exampleConflictsAppList` we can find the following list.

```
enhanced-auto-lock-door.groovy
#good-night-house.groovy
#nfc-tag-toggle.groovy
#single-button-controller.groovy
```

And, in the file `exampleConflictsAppList2` we can find the following list.

```
#enhanced-auto-lock-door.groovy
#good-night-house.groovy
nfc-tag-toggle.groovy
single-button-controller.groovy
```

The app `enhanced-auto-lock-door.groovy` is the only one uncommented in `exampleConflictsAppList`. Thus, it is taken by IoTCheck and paired up with `nfc-tag-toggle.groovy` and `single-button-controller.groovy`, as the two uncommented apps in `exampleConflictsAppList2`, to form 2 pairs: 

1. `enhanced-auto-lock-door.groovy` as `App1` and `nfc-tag-toggle.groovy` as `App2`, and
2. `enhanced-auto-lock-door.groovy` as `App1` and `single-button-controller.groovy` as `App2`.

In other words, if we just want to have IoTCheck model-check a specific pair, e.g., `enhanced-auto-lock-door.groovy` as `App1` and `nfc-tag-toggle.groovy` as `App2`, then we have to comment out `single-button-controller.groovy` in `exampleConflictsAppList2` by putting `#` in front of it. Furthermore, IoTCheck also allows the pair of apps to be executed in different orders, so we could also uncomment only `nfc-tag-toggle.groovy` in `exampleConflictsAppList` and `enhanced-auto-lock-door.groovy` in `exampleConflictsAppList2`. This will form a pair: `nfc-tag-toggle.groovy` as `App1` and `enhanced-auto-lock-door.groovy` as `App2`. After we change the app list files, we can run the `iotcheck.sh` script again. A conflict is confirmed for the two apps when IoTCheck reports a conflict for, at least, one of the two orders.

#### Device Interaction
We can run an experiment in this category by running the following command in the directory `iotcheck/smartthings-infrastructure`.

```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -d <category>
```

For example, we can run

```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -d locks
```

to run IoTCheck to check for conflicts between pairs in the `locks` group.

For some of the groups, we run the experiments for their individual sub-groups:

1. For the `Switches` group, we run the experiments for `switches`, `lightSwitches`, `acfanheaterSwitches`, `cameraSwitches`, and `ventfanSwitches`.
2. For the `Lights` group, we run the experiments for `hueLights` and `nonHueLights`.

Note: `switches` means the `Switches - General` group and `speeches` means the `Speech Synthesizers` group in the paper.

#### Global-Variable Interaction
We can run an experiment in this category by running the following command in the directory `iotcheck/smartthings-infrastructure`.

```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -g globalStateVariables
```

#### Physical-Medium Interaction
IoTCheck only [gives a warning](https://github.com/uci-plrg/iotcheck/wiki/IoTCheck-Infrastructure#iotcheck-configuration-and-preprocessing) when it detects that the interaction between apps in a pair may result in a Physical-Medium Conflict, but it does not report it as a real conflict (please see **Section 5 - Physical Factors** in the paper).

#### Further Notes
For the purpose of testing and understanding IoTCheck, we recommend running IoTCheck for categories with shorter lists of apps, e.g., `acfanheaterSwitches`, `cameraSwitches`, and `ventfanSwitches`, to see how it performs model checking and generates results reported in log files. The other categories could run for hours, days, or weeks as we have tens to hundreds of pairs and each pair is run for up to [30 minutes (or 1 hour if IoTCheck also runs with the `RandomHeuristic` search strategy)](https://github.com/uci-plrg/iotcheck/wiki/IoTCheck-JPF#jpf-main-configuration) to check for conflicts. Alternatively, the timeout could be made shorter, e.g., 5 minutes, by [changing the option in `main.jpf`](https://github.com/uci-plrg/iotcheck/wiki/IoTCheck-JPF#jpf-main-configuration).
We tabulated the results reported in the log files in each category and reported the summary in Table 6 of our paper. 

Please note that there could be 2 log files that are produced by the different orderings of the apps in the pair. For example, for the pair `good-night-house.groovy` and `nfc-tag-toggle.groovy`, there could be no conflict reported in `good-night-house.groovy--nfc-tag-toggle.groovy.log` (`good-night-house.groovy` is `App1` and `nfc-tag-toggle.groovy` is `App2`), but there could be a conflict reported in `nfc-tag-toggle.groovy--good-night-house.groovy.log` (`nfc-tag-toggle.groovy` is `App1` and `good-night-house.groovy` is `App2`). We checked both orderings in our tabulation. More details of our manual study and experimental results are documented [here](https://github.com/uci-plrg/iotcheck-data).

For more information about IoTCheck architecture explained in **Section 7** in our paper, please see the [Wiki page of this repository](https://github.com/uci-plrg/iotcheck/wiki). If you have any questions regarding IoTCheck, please do not hesitate to contact the [main author of the paper](https://rtrimana.github.io/).
