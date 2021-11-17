#!/bin/bash
mvn -B clean package -DskipTests
docker-compose up