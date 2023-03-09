FROM openjdk:19-jdk

COPY ./SAES3-Server-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/src/myapp/
COPY .env /usr/src/myapp/
COPY mykey.cert /usr/src/myapp/
COPY myKeyStore.jks /usr/src/myapp/
COPY server.cer /usr/src/myapp/
COPY serverkeystore.jks /usr/src/myapp/

WORKDIR /usr/src/myapp/
CMD ["java", "-jar", "SAES3-Server-1.0-SNAPSHOT-jar-with-dependencies.jar"]