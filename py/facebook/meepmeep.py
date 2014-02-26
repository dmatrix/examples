#!/usr/bin/env python
import sys

MEEP="Meep meep!"

if len(sys.argv) != 2:
    sys.stdout.write("Must have one argument")
    sys.stdout.write("\n")
    sys.exit(1)

print MEEP
