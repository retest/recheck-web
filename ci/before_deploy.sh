#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Extract these ######
export MVN="mvn --settings ${TRAVIS_BUILD_DIR}/.travis.settings.xml"

###### GPG ######

if [ ! -f ${TRAVIS_BUILD_DIR}/already_signed ]; then
    # decrypt ReTest secret key
    openssl aes-256-cbc -K "${encrypted_58a7d4f10478_key}" -iv "${encrypted_58a7d4f10478_iv}" -in "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc.enc" -out "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc" -d
    # import decrypted ReTest secret key
    gpg --fast-import "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc"
    # package sign artifacts
    ${MVN} verify -DskipTests -Psign
    
    touch ${TRAVIS_BUILD_DIR}/already_signed
fi
