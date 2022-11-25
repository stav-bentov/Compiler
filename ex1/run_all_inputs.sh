BASEDIR=$(shell pwd)
INPUT_DIR=${BASEDIR}input
OUTPUT_DIR=${BASEDIR}/output
INPUT=${INPUT_DIR}/input.txt
OUTPUT=${OUTPUT_DIR}/OutputTokens.txt
ls output
for FILE in ./input/*; do java -jar LEXER ${INPUT} ${OUTPUT}; done