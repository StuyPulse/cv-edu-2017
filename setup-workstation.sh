#!/bin/bash

# TODO: check if libraries are installed in /var/tmp/robo-software

software="/var/tmp/robo-software"

export JAVA_HOME="$software/jdk1.8.0_121"
export ANT_HOME="$software/apache-ant-1.10.1"
cmake_path="$software/cmake-3.8.0-rc3"
export PATH="$JAVA_HOME/bin:$ANT_HOME/bin:$cmake_path/bin:$PATH"

echo "Set JAVA_HOME to $JAVA_HOME"
echo "Set ANT_HOME to $ANT_HOME"
echo 'Added $JAVA_HOME/bin, $ANT_HOME/bin, and cmake executables to PATH'
echo "PATH is now $PATH"
echo ""

echo "Now running 'make && make run' in cv-edu-2017 should successfully build"
