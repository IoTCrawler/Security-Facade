#
#Copyright Odin Solutions S.L. All Rights Reserved.
#
#SPDX-License-Identifier: Apache-2.0
#

FROM  tomcat as maven

RUN apt update
RUN apt install -y maven git

from maven

WORKDIR /root
RUN  mkdir git_projects
WORKDIR /root/git_projects

### Capability manager Below

COPY Java_CapabilityManagerServlet ./Java_CapabilityManagerServlet
COPY Java_CapabilityTokens ./Java_CapabilityTokens
COPY XACML_HTTPClient ./XACML_HTTPClient

WORKDIR /root/git_projects/XACML_HTTPClient
RUN mvn -U clean install
RUN mvn install:install-file -Dfile=./target/pdpclient_http-1.0-SNAPSHOT-jar-with-dependencies.jar -DgroupId=PDPClient -DartifactId=PDPClient -Dversion=1.0-SNAPSHOT -Dpackaging=jar 

WORKDIR /root/git_projects/Java_CapabilityTokens
RUN mvn -U clean install
RUN mvn install:install-file -Dfile=./target/Tokens-1.0.2-SNAPSHOT.jar -DgroupId=CapabiltyTokens     -DartifactId=CapabilityTokens -Dversion=1.0.2-SNAPSHOT -Dpackaging=jar 

WORKDIR /root/git_projects/Java_CapabilityManagerServlet
RUN mvn -U clean install
RUN ls -lah target
RUN mv ./target/CapabilityManagerServlet-0.0.2-SNAPSHOT.war /usr/local/tomcat/webapps/CapabilityManagerServlet.war

WORKDIR /root/
RUN rm -rf git_projects
RUN rm -rf /root/.ssh

