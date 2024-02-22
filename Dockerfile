FROM tomcat:9.0.75-jdk11-temurin-focal


ADD /target/CatApp.war /usr/local/tomcat/webapps/

#EXPOSE 8081

#CMD ["catalina.sh", "run"]