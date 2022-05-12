#!/bin/bash

cd kafka-discord-connector
mvn clean package assembly:single

docker build -t kafka-discord-connector:latest .
cd ..
