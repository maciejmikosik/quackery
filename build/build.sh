#!/bin/bash -e

HERE=`dirname ${0}`
cd "${HERE}"
cd ..
PROJECT=`pwd`

#cleanup
rm \
  --recursive \
  --force \
  ./build/sink/building

#compile
mkdir \
  --parents \
  ./build/sink/building
javac \
  -classpath "./main/jar/junit-4.12.jar:./main/jar/hamcrest-all-1.3.jar" \
  -sourcepath "./main/java" \
  -source 1.7 \
  -target 1.7 \
  -d "./build/sink/building" \
  ./main/java/org/quackery/Build.java

#copy sources
cp \
  --recursive \
  ./main/java/. \
  ./build/sink/building

#zip jar
cd ./build/sink/building
zip \
  --quiet \
  --recurse-paths \
  ./quackery.jar \
  ./*
cd "${PROJECT}"

#copy quackery.jar
cp \
  ./build/sink/building/quackery.jar \
  ./build/sink

echo ""
echo "BUILD SUCCESSFUL"
echo "created ${PROJECT}/build/sink/quackery.jar"

#cleanup
rm \
  --recursive \
  --force \
  ./build/sink/building
