###############
# DIRECTORIES #
###############
BASEDIR           = $(shell pwd)
JFlex_DIR         = ${BASEDIR}/jflex
SRC_DIR           = ${BASEDIR}/src
BIN_DIR           = ${BASEDIR}/bin
EXTERNAL_JARS_DIR = ${BASEDIR}/external_jars
MANIFEST_DIR      = ${BASEDIR}/manifest

#########
# FILES #
#########
JFlex_GENERATED_FILE = ${SRC_DIR}/Lexer.java
SRC_FILES            = ${SRC_DIR}/*.java
CLASS_FILES          = ${BIN_DIR}/*.class
EXTERNAL_JAR_FILES   = ${EXTERNAL_JARS_DIR}/java-cup-11b-runtime.jar
MANIFEST_FILE        = ${MANIFEST_DIR}/MANIFEST.MF

########################
# DEFINITIONS :: JFlex #
########################
JFlex_PROGRAM  = jflex
JFlex_FLAGS    = -q
JFlex_DEST_DIR = ${SRC_DIR}
JFlex_FILE     = ${JFlex_DIR}/LEX_FILE.lex

all:
	clear
	@echo "* [0] Remove LEXER program *"
	rm -rf LEXER
	@echo "* [1] Remove *.class files and JFlex generated Lexer.java *"
	rm -rf ${JFlex_GENERATED_FILE} ${CLASS_FILES}
	@echo "* [2] Use JFlex to synthesize Lexer.java from LEX_FILE.lex *"
	$(JFlex_PROGRAM) ${JFlex_FLAGS} -d ${JFlex_DEST_DIR} ${JFlex_FILE}
	@echo "* [3] Create *.class files from *.java files + External *.jar files *"
	javac -cp ${EXTERNAL_JAR_FILES} -d ${BIN_DIR} ${SRC_FILES}
	@echo "* [4] Create a JAR file from from *.class files + External *.jar files *"
	jar cfm LEXER ${MANIFEST_FILE} -C ${BIN_DIR} .