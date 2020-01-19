#!/bin/bash

source hosts.sh

echo "Creating new network for service..."
docker network create --driver overlay --scope swarm cassandra-net

echo "Deploying emergencies service stack..."
docker stack deploy -c emergencies-stack.yaml emergencies-cluster
