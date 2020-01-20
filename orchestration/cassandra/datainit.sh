#!/bin/bash

cqlsh -f create_schema.cql localhost 9042
cqlsh -f load_data.cql localhost 9042
