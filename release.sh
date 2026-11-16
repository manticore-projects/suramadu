#!/usr/bin/env bash
set -euo pipefail

./gradlew clean ultraclean
./gradlew --init-script init-cyclonedx.gradle build

