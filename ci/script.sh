#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######
export MOZ_HEADLESS=1
export $(dbus-launch)
export NSS_USE_SHARED_DB=ENABLED
export MVN="mvn --settings ${TRAVIS_BUILD_DIR}/.travis.settings.xml"

###### Maven
if [ ${TRAVIS_SECURE_ENV_VARS} = "true" ]; then
    ${MVN} clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar
else
    ${MVN} clean org.jacoco:jacoco-maven-plugin:prepare-agent verify
fi
