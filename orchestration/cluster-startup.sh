#!/bin/bash

source hosts.sh
#
#echo "Setting up docker image local registry..."
#docker container run -d --restart=always --name registry \
#  -v "$(pwd)"/certs:/certs \
#  -e REGISTRY_HTTP_ADDR="${REGISTRY}:5000" \
#  -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
#  -e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
#  -p 5000:5000 \
#  registry:2
#
#echo "Building emergencies service container..."
#docker image build -t emergencies-service ../service
#
##echo "Pushing emergencies-service image to local registry..."
#docker tag emergencies-service localhost:5000/emergencies-service
#docker push localhost:5000/emergencies-service

echo "Creating network for service..."
docker network create --driver overlay --scope swarm cassandra-net

echo "Deploying emergencies service stack..."
docker stack deploy -c emergencies-stack.yaml emergencies-cluster
