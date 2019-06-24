#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Maven ######
mvn ${MVN_ARGS} deploy -DskipTests -Psign
