#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######
export MVN="mvn --settings ${TRAVIS_BUILD_DIR}/.travis.settings.xml"

###### Maven ######
${MVN} deploy -DskipTests -Psign
