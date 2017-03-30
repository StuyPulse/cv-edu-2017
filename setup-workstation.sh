#!/bin/bash

export JAVA_HOME="/home/students/2019/robotics/Software/jdk1.8.0_121"
export ANT_HOME="/home/students/2019/robotics/Software/apache-ant-1.10.1"
cmake_path="/home/students/2019/robotics/Software/cmake-3.8.0-rc3"
export PATH="$JAVA_HOME/bin:$ANT_HOME/bin:$cmake_path/bin:$PATH"

echo "Set JAVA_HOME to $JAVA_HOME"
echo "Set ANT_HOME to $ANT_HOME"
echo 'Added $JAVA_HOME/bin, $ANT_HOME/bin, and cmake executables to PATH'
echo "PATH is now $PATH"
echo ""

if [[ "$(pwd)" =~ cv-edu-2017$ && ! -d lib/opencv-3.2.0 ]]; then
    cv_path=/home/students/2019/robotics/Software/opencv-3.2.0 
    ln -s $cv_path lib/
    echo "Linked lib/opencv-3.2.0 to $cv_path"
fi

echo "Now running 'make && make run' in cv-edu-2017 should successfully build"
