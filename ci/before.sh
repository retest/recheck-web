#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######

GECKODRIVER_VERSION="v0.26.0"
GECKODRIVER_HASH="7a908350c9ce8bb90d9a8293c5758301"

###### Chrome ######

ln --symbolic /usr/lib/chromium-browser/chromedriver "${HOME}/bin/chromedriver"

###### Firefox ######
wget --quiet https://github.com/mozilla/geckodriver/releases/download/${GECKODRIVER_VERSION}/geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz
echo ${GECKODRIVER_HASH} "geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz" | md5sum --check -
tar xf "geckodriver-${GECKODRIVER_VERSION}-linux64.tar.gz" -C ${HOME}/bin/
