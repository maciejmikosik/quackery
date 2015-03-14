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
  -classpath "./jar/junit-4.12.jar:./jar/hamcrest-core-1.3.jar" \
  -sourcepath "./java" \
  -source 1.7 \
  -target 1.7 \
  -d "./sink/building" \
  ./java/org/quackery/Build.java

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
  ./quackery.jar \
  ./*
cd $MAIN

#copy quackery.jar
cp \
  ./sink/building/quackery.jar \
  ./sink

echo ""
echo "BUILD SUCCESSFUL"
echo "created $MAIN/sink/quackery.jar"

#cleanup
rm \
  --recursive \
  --force \
  ./sink/building
