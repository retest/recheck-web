#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### GPG ######

if [ ! -f ${TRAVIS_BUILD_DIR}/already_signed ]; then
    # decrypt ReTest secret key
    openssl aes-256-cbc -K "${encrypted_5faff216b858_key}" -iv "${encrypted_5faff216b858_iv}" -in "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc.enc" -out "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc" -d
    # import decrypted ReTest secret key
    gpg --fast-import "${TRAVIS_BUILD_DIR}/retest-gmbh-gpg.asc"
    # package sign artifacts
    mvn ${MVN_ARGS} verify -DskipTests -Psign
    
    touch ${TRAVIS_BUILD_DIR}/already_signed
fi
