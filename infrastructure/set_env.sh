#!/bin/bash

if [[ -z "${TRAVIS_BUILD_NUMBER}" ]]; then
  export VERSION=0.0.1-SNAPSHOT
else
  export VERSION=${TRAVIS_BUILD_NUMBER}
fi

if [[ -z "${PORT}" ]]; then
  export PORT=8000
  echo "env - Using default PORT"
fi

if [[ -z "${MB_URL}" ]]; then
  export MB_URL="tcp://localhost:61616?jms.redeliveryPolicy.maximumRedeliveries=1"
  echo "env - Using default MB_URL"
fi

if [[ -z "${MB_USER}" ]]; then
  export MB_USER=admin
  echo "env - Using default MB_USER"
fi

if [[ -z "${MB_PASSWORD}" ]]; then
  export MB_PASSWORD=password
  echo "env - Using default MB_PASSWORD"
fi

if [[ -z "${DB_URL}" ]]; then
  export DB_URL="jdbc:postgresql://localhost:5455/mttw"
  echo "env - Using default DB_URL"
fi

if [[ -z "${DB_USER}" ]]; then
  export DB_USER=user
  echo "env - Using default DB_USER"
fi

if [[ -z "${DB_PASSWORD}" ]]; then
  export DB_PASSWORD=password
  echo "env - Using default DB_PASSWORD"
fi

if [[ -z "${GC_APP_ID}" ]]; then
  export GC_APP_ID=mttw
  echo "env - Using default GC_APP_ID"
fi

if [[ -z "${GC_KEY}" ]]; then
  export GC_KEY=supersecret
  echo "env - Using default GC_KEY"
fi
