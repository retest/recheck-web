#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######
export MOZ_HEADLESS=1
export $(dbus-launch)
export NSS_USE_SHARED_DB=ENABLED

###### Maven ######
# Compile with JDK 8
mvn ${MVN_ARGS} clean package -DskipTests

# Test with JDK 11
wget --quiet https://github.com/sormuras/bach/raw/master/install-jdk.sh && . ./install-jdk.sh -F 11

if [ ${TRAVIS_SECURE_ENV_VARS} = "true" ]; then
    mvn ${MVN_ARGS} clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar
else
    mvn ${MVN_ARGS} clean org.jacoco:jacoco-maven-plugin:prepare-agent verify
fi
