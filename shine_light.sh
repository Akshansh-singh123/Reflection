#!/bin/bash

set -e

MODULE_NAME=""

# Parse flags
while [[ $# -gt 0 ]]; do
  case $1 in
    -m|--module)
      MODULE_NAME="$2"
      shift 2
      ;;
  esac
done

if [[ -z "$MODULE_NAME" ]]; then
  echo "Module name not specified, add it as -m flag (eg ./shine_light.sh -m featureModule)"
  exit 1
fi

echo "Running Gradle task: $MODULE_NAME:featureJar"
./gradlew "$MODULE_NAME:featureJar"

echo "Running Gradle task: core"
./gradlew core:build

./gradlew shadowJar

JAR_PATH="./build/libs/reflect-gen.jar"

if [[ ! -f "$JAR_PATH" ]]; then
  echo "Fat JAR not found at $JAR_PATH"
  exit 1
fi

echo "Running $JAR_PATH..."
java -jar "$JAR_PATH" "$MODULE_NAME"