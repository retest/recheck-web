#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

BUCKET='DO:golden-master'
URL="https://downloads.rclone.org/${RCLONE_VERSION}/rclone-${RCLONE_VERSION}-linux-amd64.zip"

SOURCE_PATH='src/test/resources/retest/recheck/'
TARGET_PATH='target/test-classes/retest/recheck/'

echo "Downloading rclone ..."
wget --quiet ${URL}
echo ${RCLONE_HASH} "rclone-${RCLONE_VERSION}-linux-amd64.zip" | md5sum --check -
unzip -j -q "rclone-${RCLONE_VERSION}-linux-amd64.zip" "rclone-${RCLONE_VERSION}-linux-amd64/rclone" -d ${HOME}/bin/

echo "Uploading to S3 ..."
rclone copy ${TRAVIS_BUILD_DIR}/${SOURCE_PATH} ${BUCKET}/${TRAVIS_REPO_SLUG}/${TRAVIS_BRANCH}/${TRAVIS_BUILD_NUMBER}/source
rclone copy ${TRAVIS_BUILD_DIR}/${TARGET_PATH} ${BUCKET}/${TRAVIS_REPO_SLUG}/${TRAVIS_BRANCH}/${TRAVIS_BUILD_NUMBER}/target

exit 0
