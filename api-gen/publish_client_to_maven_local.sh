#!/bin/bash

export JAVA_HOME="/home/evhen/.sdkman/candidates/java/current"

cd build/api-generated || exit

/home/evhen/.sdkman/candidates/maven/current/bin/mvn clean install