# Emergencies Handling Platform

## Overview
A simulation platform for handling emergencies by fire brigade. It's purpose is to assign fire trucks to reported emergencies.
The Cassandra database is used as a backing registry of all fire trucks, either idle or in action.
The platform should assign trucks in a valid way, that is a single truck is not assigned to two different emergencies at the same time.

## System setup

### Requirements
* Docker 1.12+

### Docker Swarm
1. Run `docker swarm init --advertise-addr <MANAGER_IP>` to initialize the Swarm manager node.
2. Run `docker swarm join-token worker` to find out the command which workers will execute to join the swarm.
3. Run the aforementioned command to join workers to the swarm.
4. Run `docker node ls` on the manager node to find out if all nodes are up-and-running.
