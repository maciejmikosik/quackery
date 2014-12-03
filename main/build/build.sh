#!/bin/bash -e

BUILD_DIR=`dirname ${0}`
cd "${BUILD_DIR}/.."
MAIN=`pwd`

#cleanup
rm \
  --recursive \
  --force \
  ./sink/building

#compile
mkdir \
  --parents \
  ./sink/building
javac \
  -classpath "./jar/junit-4.11.jar" \
  -sourcepath "./java" \
  -source 1.7 \
  -target 1.7 \
  -d "./sink/building" \
  ./java/org/testanza/Build.java

#copy sources
cp \
  --recursive \
  ./java/. \
  ./sink/building

#zip jar
cd ./sink/building
zip \
  --quiet \
  --recurse-paths \
  ./testanza.jar \
  ./*
cd $MAIN

#copy testanza.jar
cp \
  ./sink/building/testanza.jar \
  ./sink

echo ""
echo "BUILD SUCCESSFUL"
echo "created $MAIN/sink/testanza.jar"

#cleanup
rm \
  --recursive \
  --force \
  ./sink/building
