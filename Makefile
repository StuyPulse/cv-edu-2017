PROJECT_ROOT = $(shell pwd)

BUILDDIR = $(PROJECT_ROOT)/build
SRCDIR = $(PROJECT_ROOT)/src

LIB = $(PROJECT_ROOT)/lib
OPENCV = /var/tmp/opencv-3.0.0

OPENCV_LIBS = $(OPENCV)/build/lib
OPENCV_JAR = $(OPENCV)/build/bin/opencv-300.jar
JAVAFX = /var/tmp/robo-libs/jfxrt.jar

COMPILE = javac -g -d $(BUILDDIR) -cp $(SRCDIR):lib/stuyvision.jar:$(OPENCV_JAR):$(JAVAFX)

RUN_CLASSPATH = $(BUILDDIR):lib/stuyvision.jar:$(OPENCV_JAR):$(JAVAFX)

class = Main

compile: clean
	mkdir build
	$(COMPILE) $(SRCDIR)/$(class).java

run: compile
	java -Djava.library.path=$(OPENCV_LIBS) -cp "$(RUN_CLASSPATH)" $(class)

clean:
	rm -rf build
