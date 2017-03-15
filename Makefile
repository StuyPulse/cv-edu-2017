PROJECT_ROOT = $(shell pwd)

LIB = $(PROJECT_ROOT)/lib

STUYVISION = $(LIB)/stuyvision.jar
JAVAFX = $(LIB)/jfxrt.jar

SRC = $(PROJECT_ROOT)/src
BIN = $(PROJECT_ROOT)/build

OPENCV = $(PROJECT_ROOT)/lib/opencv-3.0.0
OPENCV_JAR = $(OPENCV)/build/bin/opencv-300.jar
OPENCV_LIBS = $(OPENCV)/build/lib/

JAVAC = javac
CLASSPATH = $(BIN):$(STUYVISION):$(JAVAFX):$(OPENCV_JAR)
JAVA_FLAGS = -g -d $(BIN) -cp $(CLASSPATH)
COMPILE = $(JAVAC) $(JAVA_FLAGS)

.DEFAULT_GOAL := all

class=Main

all: clean init-bin
	find $(SRC) -name '*.java' -print | xargs $(COMPILE)

clean:
	rm -rf $(BIN)

init-bin:
	mkdir $(BIN)

run:
	cd build && java -cp $(CLASSPATH) -Djava.library.path=$(OPENCV_LIBS) $(class)
