#!/bin/bash

docker-compose -f infrastructure/docker-compose-app.yml build

docker tag infrastructure_app eu.gcr.io/"$GC_APP_ID"/mttw_app:"$VERSION"

echo "$GC_KEY" | base64 --decode -i >"$HOME"/gcloud-service-key.json

gcloud auth activate-service-account --key-file "${HOME}"/gcloud-service-key.json

gcloud docker --push eu.gcr.io/"$GC_APP_ID"/mttw_app:"$VERSION"
