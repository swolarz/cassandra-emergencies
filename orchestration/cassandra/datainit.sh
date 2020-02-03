#!/bin/bash

if [[ $# -ne 1 ]]; then
	echo "Usage: datainit <CASSY_HOST>"
	exit 2
fi

cqlsh -f dbinit.cql "$1" 9042
