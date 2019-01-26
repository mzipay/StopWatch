P      := StopWatch
SEMVER := 1.0.85

JAVA_HOME := /etc/alternatives/java_sdk_1.8.0_openjdk
JAVAC     := ${JAVA_HOME}/bin/javac
JAVA      := ${JAVA_HOME}/bin/java
JAR       := ${JAVA_HOME}/bin/jar

SOURCEPATH  := trunk/src
CLASSPATH   := ${SOURCEPATH}:hsqldb-1.8.0.10.jar:DateChooser-1.2.jar
CLASSES_DIR := classes
DIST_DIR    := dist

JFLAGS  = -g -source 1.5 -target 1.5 -cp ${CLASSPATH} -sourcepath ${SOURCEPATH} -d ${CLASSES_DIR}

SOURCES       = $(shell find ${SOURCEPATH} -type f -name '*.java')
PROPERTIES    = $(shell find ${SOURCEPATH} -type f -name '*.properties')
CLASSES       = ${SOURCES:${SOURCEPATH}/%.java=${CLASSES_DIR}/%.class}
RESOURCES     = ${PROPERTIES} ${SOURCEPATH}/stopwatchdb.script ${SOURCEPATH}/usage.txt
JAR_RESOURCES = ${RESOURCES:${SOURCEPATH}/%=${CLASSES_DIR}/%}

${DIST_DIR}/${P}-${SEMVER}.jar: ${CLASSES} ${JAR_RESOURCES} manifest.txt
	@mkdir -p ${CLASSES_DIR}
	@unzip -q -o DateChooser-1.2.jar -d ${CLASSES_DIR}
	@unzip -q -o hsqldb-1.8.0.10.jar -d ${CLASSES_DIR}
	@mkdir -p ${DIST_DIR}
	$(JAR) cfm ${DIST_DIR}/${P}-${SEMVER}.jar manifest.txt -C ${CLASSES_DIR} .
	@cp ${SOURCEPATH}/stopwatchdb.properties ${SOURCEPATH}/stopwatchdb.script .

${CLASSES_DIR}/%.class: ${SOURCEPATH}/%.java
	@mkdir -p ${CLASSES_DIR}
	${JAVAC} ${JFLAGS} $<

${CLASSES_DIR}/%: ${SOURCEPATH}/%
	@mkdir -p ${@D}
	@cp $< $@

.PHONY: clean distclean run

clean:
	@rm -rf classes

distclean: clean
	@rm -rf dist
	@rm -f stopwatchdb.*
	@rm -f stopwatch.log

run: ${DIST_DIR}/${P}-${SEMVER}.jar
	${JAVA} -jar ${DIST_DIR}/${P}-${SEMVER}.jar &>/dev/null &

