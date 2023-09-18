#!/bin/bash

mv pom.xml pom.xml.pro
mv pom.xml.dev pom.xml

ls -r
# pname=$(basename $(dirname "$PWD"))
pname=$( cat ./../pom.xml | grep \<\/artifactId\> -m 1 | cut -d'>' -f2 | cut -d'<' -f1 |  tr -d ' ')
echo pname=${pname}
sed -i "s/P_ARTIFACTID/${pname}/g" $(grep P_ARTIFACTID -rl ./pom.xml)

pversion=$( cat ./../pom.xml | grep \<\/version\> -m 1 | cut -d'>' -f2 | cut -d'<' -f1 |  tr -d ' ')
echo pversion=${pversion}
sed -i "s/P_VERSION/${pversion}/g" $(grep P_VERSION -rl ./pom.xml)
