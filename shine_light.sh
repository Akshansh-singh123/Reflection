#!/bin/bash

set -e

MODULE_NAME=""

# Name of the Kotlin file
KOTLIN_FILE="LightSource"
KOTLIN_SRC="./src/main/kotlin/com/akshansh/app/${KOTLIN_FILE}.kt"
KOTLIN_OUT="./src/main/kotlin/com/akshansh/app/${KOTLIN_FILE}.jar"
KOTLIN_HOME=$(dirname $(which kotlinc))/../lib
KOTLIN_LIBS="$KOTLIN_HOME/kotlin-reflect.jar"
ANNOTATIONS_JAR="./core/build/libs/core-1.0-SNAPSHOT.jar"

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

## Compile
#echo "Shining light..."
#kotlinc "$KOTLIN_SRC" -classpath "$ANNOTATIONS_JAR:$KOTLIN_LIBS" -include-runtime -d "$KOTLIN_OUT"
#
## Run
#if [ $? -eq 0 ]; then
#  echo "Running $KOTLIN_OUT..."
#  java -jar "$KOTLIN_OUT"
#else
#  echo "Compilation failed."
#fi

if [[ ! -f "$JAR_PATH" ]]; then
  echo "Fat JAR not found at $JAR_PATH"
  exit 1
fi

echo "Running $JAR_PATH..."
java -jar "$JAR_PATH" "$MODULE_NAME"