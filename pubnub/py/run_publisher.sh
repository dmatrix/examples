#!/bin/sh
a=0
while [ $a -lt 10 ]
do
   echo $a
   a=`expr $a + 1`
   python publish_devices.py -n 10 -c devices -i 1 -d data_dir/
   sleep 15
done
