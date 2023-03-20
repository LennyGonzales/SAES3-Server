FROM openjdk:19-jdk

COPY ./SAES3-Server-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/src/myapp/
COPY .env /usr/src/myapp/
COPY keyStore.jks /usr/src/myapp/
COPY trustStore.jts /usr/src/myapp/

WORKDIR /usr/src/myapp/
CMD ["java", "-jar", "SAES3-Server-1.0-SNAPSHOT-jar-with-dependencies.jar"]