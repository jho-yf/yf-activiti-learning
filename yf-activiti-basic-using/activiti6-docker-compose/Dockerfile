FROM tomcat:9.0.44-jdk8-openjdk-slim

WORKDIR /usr/local/tomcat/webapps/ROOT

RUN rm -rf *

ADD activiti-admin.war  /usr/local/tomcat/webapps
ADD activiti-app.war  /usr/local/tomcat/webapps
ADD activiti-rest.war  /usr/local/tomcat/webapps

WORKDIR /usr/local/tomcat
