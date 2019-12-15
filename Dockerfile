FROM openjdk:8u212-jdk-slim

ARG PORT

VOLUME /tmp

EXPOSE ${PORT}

ADD target/mttw-0.0.1-SNAPSHOT.jar mttw.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mttw.jar"]