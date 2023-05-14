#!/bin/sh

ls webapps/activiti-admin
ls webapps/activiti-app
ls webapps/activiti-rest

cp /properties/activiti-admin.properties /usr/local/tomcat/webapps/activiti-admin/WEB-INF/classes/META-INF/activiti-admin/activiti-admin.properties
cp /properties/activiti-app.properties /usr/local/tomcat/webapps/activiti-app/WEB-INF/classes/META-INF/activiti-app/activiti-app.properties
cp /properties/engine.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/engine.properties
cp /properties/db.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/db.properties

sh ./bin/catalina.sh run

exec "$@"