FROM tomcat:9.0.44-jdk8-openjdk-slim

ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

WORKDIR /usr/local/tomcat/webapps

RUN rm -rf *

ADD activiti-admin.war  /usr/local/tomcat/webapps
ADD activiti-app.war  /usr/local/tomcat/webapps
ADD activiti-rest.war  /usr/local/tomcat/webapps

RUN mkdir activiti-admin && cd activiti-admin && jar -xvf ../activiti-admin.war && rm -f ../activiti-admin.war
RUN mkdir activiti-app && cd activiti-app && jar -xvf ../activiti-app.war && rm -f ../activiti-app.war
RUN mkdir activiti-rest && cd activiti-rest && jar -xvf ../activiti-rest.war && rm -f ../activiti-rest.war

WORKDIR /
RUN mkdir properties
COPY ./properties/* properties/
RUN chmod 777 properties/*

WORKDIR /usr/local/tomcat
RUN mkdir script
COPY ./script/* script/
RUN chmod 777 script/*

EXPOSE 8080
CMD ["sh", "script/startup.sh"]
