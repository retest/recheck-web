#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######

GECKODRIVER_VERSION="v0.24.0"
GECKODRIVER_HASH="7552b85e43973c84763e212af7cca566"

###### Chrome ######

ln --symbolic /usr/lib/chromium-browser/chromedriver "${HOME}/bin/chromedriver"

###### Firefox ######
wget --quiet https://github.com/mozilla/geckodriver/releases/download/${GECKODRIVER_VERSION}/geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz
echo ${GECKODRIVER_HASH} "geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz" | md5sum --check -
tar xf "geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz" -C ${HOME}/bin/
