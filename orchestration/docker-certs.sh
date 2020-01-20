#!/bin/bash

DOCKER_CERTS="/etc/docker/certs.d/${REGISTRY}:5000"

mkdir -p "$DOCKER_CERTS"
cp certs/domain.crt certs/domain.key "$DOCKER_CERTS/"
mv "$DOCKER_CERTS/domain.crt" "$DOCKER_CERTS/ca.crt"

service docker reload
