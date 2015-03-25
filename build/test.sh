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
  -sourcepath "./main/java:./test/java" \
  -source 1.7 \
  -target 1.7 \
  -d "./build/sink/building" \
  ./main/java/org/quackery/Build.java \
  ./test/java/org/quackery/test_all.java

java \
  -classpath "./build/sink/building:./main/jar/junit-4.12.jar:./main/jar/hamcrest-all-1.3.jar" \
  org.quackery.test_all

#cleanup
rm \
  --recursive \
  --force \
  ./build/sink/building
