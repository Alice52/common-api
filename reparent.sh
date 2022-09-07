#!/bin/bash

rm pom.xml
mv pom.xml.dev pom.xml

ls -r
pname=$(basename $(dirname $(dirname "$PWD")))
sed -i "s/P_ARTIFACTID/${pname}/g" $(grep P_ARTIFACTID -rl ./pom.xml)

pversion=$(cat pom.xml | grep \<\/version\> -m 1 | cut -d'<' -f1 | tr -d ' ')
sed -i "s/P_VERSION/${pversion}/g" $(grep P_VERSION -rl ./pom.xml)
