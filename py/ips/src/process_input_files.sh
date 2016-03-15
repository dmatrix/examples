#!/bin/sh
#
# process all ip files in the data directory
#
for f in ../data/ip_input.* 
do
	echo "***Processing ip file $f"
	python extract_ips_info.py -i $f
	if [ $? == 0 ]; then
		/bin/rm -f $f
		echo "---Removed ip file $f"
	fi
done
